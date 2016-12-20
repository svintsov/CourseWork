package ua.codeconveyor.coursework.model.engine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ua.codeconveyor.coursework.model.entity.Node;
import ua.codeconveyor.coursework.view.ChannelView;
import ua.codeconveyor.coursework.view.NodeView;


public class SurfaceEngine extends SurfaceView implements SurfaceHolder.Callback {
    private OffsetThread mainThread;
    private NodeView node;
    private List<NodeView> containerNode;
    private int nodeCounter = 0;
    private Context context;
    private ChannelView.ChannelType chanType;
    private final int REGION_COUNTER = 3;

    /* Buttons flags */
    private boolean isAdd = false;
    private boolean isDel = false;
    private boolean isAddChannel = false;
    private List<NodeView> nodeAddChannels;

    /* Scaling fields */
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private Rect rect;

    /* Drag fields*/
    private float lastX;
    private float lastY;

    private Random random;


    public SurfaceEngine(Context context) {
        super(context);
        random = new Random();
        this.context = context;
        // Сообщаем, что обработчик событий от поверхности будет реализован
        // в этом классе.
        getHolder().addCallback(this);
        containerNode = new LinkedList<>();
        nodeAddChannels = new LinkedList<>();

        drawStartGraph();

        setFocusable(true);

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

    }

    private void drawStartGraph() {
        drawRegionNodes();
        drawAllRegionEdges();

    }

    private void drawAllRegionEdges() {

        for (int i = 1; i <= REGION_COUNTER; i++) {
            drawRegionEdges(i);
        }
    }

    private void drawRegionEdges(int currentRegion) {
        List<NodeView> regionNodes = getRegionNodes(currentRegion);
        int edgesCounter = 2;
        while (edgesCounter / regionNodes.size() < 3.5) {
            NodeView nodeView1 = regionNodes.get(random.nextInt(regionNodes.size()));
            NodeView nodeView2 = regionNodes.get(random.nextInt(regionNodes.size()));
            if (nodeView1 != nodeView2) {
                if (nodeView1.getNode().isHaveCommonEdge(nodeView2.getNode()) != null) {
                    nodeView1.removeChannelWith(nodeView2);
                    edgesCounter--;
                }
                nodeView1.addChannelWith(nodeView2, ChannelView.ChannelType.NORMAL);
                edgesCounter++;

            }
        }
    }

    private List<NodeView> getRegionNodes(int region) {
        List<NodeView> regionNodes = new ArrayList<>();
        switch (region) {
            case 1: {
                for (NodeView nodeView : containerNode) {
                    if (nodeView.getNode().getRegion().equals("Region1")) {
                        regionNodes.add(nodeView);
                    }
                }
                break;
            }
            case 2: {
                for (NodeView nodeView : containerNode) {
                    if (nodeView.getNode().getRegion().equals("Region2")) {
                        regionNodes.add(nodeView);
                    }
                }
                break;
            }
            case 3: {
                for (NodeView nodeView : containerNode) {
                    if (nodeView.getNode().getRegion().equals("Region3")) {
                        regionNodes.add(nodeView);
                    }
                }
                break;
            }
        }
        return regionNodes;

    }

    private void drawLocalNodes(int x, int y, String region) {
        for (int i = 0; i < 8; i++) {
            int Ly = (int) (Math.random() * 1400) - 700;
            int Lx = (int) (Math.random() * 1400) - 700;
            addNode(x + Lx, y + Ly, region);
        }
    }

    private void drawRegionNodes() {
        int k = 2000;
        int x = 300;
        int y = 300;
        NodeView centreNode1 = addNode(x, y, "Region1");
        drawLocalNodes(x, y, "Region1");
        NodeView centralNode2 = addNode(x + k, x + k, "Region2");
        drawLocalNodes(x + k, x + k, "Region2");
        NodeView centralNode3 = addNode(x + 2 * k, y, "Region3");
        drawLocalNodes(x + 2 * k, y, "Region3");
        centreNode1.addChannelWith(centralNode2, ChannelView.ChannelType.NORMAL);
        centreNode1.addChannelWith(centralNode3, ChannelView.ChannelType.SATELLITE);
        centralNode2.addChannelWith(centralNode3, ChannelView.ChannelType.SATELLITE);
    }

    public NodeView addNode(int x, int y, String region) {
        Node node = new Node(new Point(x, y), region);
        node.setId(nodeCounter++);
        NodeView nodeView = new NodeView(node, context);
        containerNode.add(nodeView);
        return nodeView;
    }

    public void removeNode(NodeView node) {
        containerNode.remove(node);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mainThread = new OffsetThread(getHolder(), this);
        mainThread.setRunning(true);
        mainThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //посылаем потоку команду на закрытие и дожидаемся,
        //пока поток не будет закрыт.
        boolean retry = true;
        while (retry) {
            try {
                mainThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // пытаемся снова остановить поток thread
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        float px = event.getX() / mScaleFactor + rect.left;
        float py = event.getY() / mScaleFactor + rect.top;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // вызываем метод handleActionDown, куда передаем координаты касания
            for (NodeView node : containerNode) {
                boolean isDown = node.handleActionDown((int) px, (int) py);
                if (isDown) {
                    if (isAddChannel) {
                        if (!(nodeAddChannels.size() == 1 && nodeAddChannels.get(0).equals(node))) {
                            nodeAddChannels.add(node);
                        }
                    } else {
                        this.node = node;
                    }
                    break;
                } else {
                    this.node = null;
                }
            }
            // если щелчок по нижней области экрана, то выходим
            if (event.getY() > getHeight() - 50) {
                mainThread.setRunning(false);
                ((Activity) getContext()).finish();
            } else {
                processTouchButtons((int) px, (int) py);
            }
        }
        if (this.node != null) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                // перемещение
                if (node.isTouched()) {
                    // робот находится в состоянии перетаскивания,
                    // поэтому изменяем его координаты
                    node.setX((int) px);
                    node.setY((int) py);
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Обрабатываем отпускание
                if (node.isTouched()) {
                    node.setTouched(false);
                }
            }
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (!containerNode.isEmpty()) {
                    if (Math.abs(px - lastX) <= 100 && Math.abs(py - lastY) <= 100) {
                        for (NodeView node : containerNode) {

                            node.setX((int) (node.getNode().getX() + (px - lastX)));
                            node.setY((int) (node.getNode().getY() + (py - lastY)));
                            //node.translateNode((px - lastX), (py - lastY));
                        }
                    }
                }
                lastX = px;
                lastY = py;
            }
        }
        return true;
    }

    private void processTouchButtons(int px, int py) {
        if (isAdd) {
            setAdd(false);
            addNode(px, py, "Region1");
        } else if (isDel) {
            setDel(false);
            removeNode(node);
        } else if (isAddChannel && nodeAddChannels.size() == 2) {
            setAddChannel(false, chanType);
            for (NodeView nodeView : containerNode) {
                if (nodeView.equals(nodeAddChannels.get(0))) {
                    nodeView.addChannelWith(nodeAddChannels.get(1), chanType);
                    break;
                }
            }
            nodeAddChannels.clear();
        }
        mainThread.interrupt();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.scale(mScaleFactor, mScaleFactor);
        rect = canvas.getClipBounds();
        for (int i = 0; i < containerNode.size(); i++) {

            for (int j = 0; j < containerNode.get(i).getChannelViewList().size(); j++) {
                containerNode.get(i).getChannelViewList().get(j).draw(canvas);
            }
            containerNode.get(i).draw(canvas);
        }
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }

    public void setAdd(boolean add) {
        setAllFlagsFalse();
        this.isAdd = add;
    }

    public void setDel(boolean del) {
        setAllFlagsFalse();
        isDel = del;
    }

    public void setAddChannel(boolean channel, ChannelView.ChannelType channelType) {
        setAllFlagsFalse();
        isAddChannel = channel;
        chanType = channelType;
    }

    public void setAllFlagsFalse() {
        isAdd = false;
        isDel = false;
        isAddChannel = false;
    }

    public void pauseThread() {
        mainThread.setRunning(false);
    }

    public void resumeThread() {
        mainThread.setRunning(true);
    }

    public Node[] getNodesContainer() {
        Node[] result = new Node[containerNode.size()];
        for (int i = 0; i < containerNode.size(); i++) {
            result[i] = containerNode.get(i).getNode();
        }
        return result;
    }
}
