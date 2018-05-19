package main.groovy.smoh

import main.java.com.utils.NodeType

class TNode extends HBase {
    public String ip

    public TNode(String caption, String ip)
    {
        super(caption);
        this.type = NodeType.CAN;
        this.ip = ip
    }

    public <T> void connect(T obj, Integer connectionspeed, SpeedUnit speedunt, Integer linkLgth)
    {
        connected = obj.caption;
        speed = connectionspeed;
        speedUnit = speedunt;
        linkLength = linkLgth;
    }
}
