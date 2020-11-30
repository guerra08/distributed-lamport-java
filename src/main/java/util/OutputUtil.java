package util;

import java.util.Date;

public class OutputUtil {

    public static String generateLocalEventOutput(Integer localNodeId, Integer localClockValue){
        return String.format("%d %d %s l", new Date().getTime(), localNodeId, localClockValue + "" + localNodeId);
    }

    public static String generateSendEventOutput(Integer localNodeId, Integer sentClockValue, Integer recipientNodeId){
        return String.format("%d %d %s s %d", new Date().getTime(), localNodeId,
                sentClockValue + "" + localNodeId, recipientNodeId);
    }

    public static String generateReceivedEventOutput(Integer localNodeId, Integer clockAfterReceive, Integer senderNodeId, Integer receivedClock){
        return String.format("%d %d %d r %d %d", new Date().getTime(), localNodeId, clockAfterReceive, senderNodeId, receivedClock);
    }

}
