package net.onbee.eldesthub.nmdc;

import net.onbee.eldesthub.Configuration;
import net.onbee.eldesthub.Logging;
import net.onbee.eldesthub.Translation;

public class NMDCProtocol {

    public final static char COMMAND_END_CHAR = '|';
    public final static char COMMAND_SEP_CHAR = ' ';
    public final static char COMMAND_BEG_CHAR = '$';
    
    private static Logging l = Logging.getInstance();
    private static Configuration c = Configuration.getInstance();
    private static Translation t = Translation.getInstance();

    public static void doCommand(NMDCUser u, String data) {
        int type = NMDCCommands.getType(data);
        switch (type) {
            case NMDCCommands.Chat:
                NMDCEvents.eventChat(u, data);
                break;
            //case NMDCCommands.Close:
            case NMDCCommands.ConnectToMe:
                NMDCEvents.eventConnectToMe(u, data);
                break;
            case NMDCCommands.GetINFO:
                NMDCEvents.eventGetINFO(u, data);
                break;
            case NMDCCommands.GetNickList:
                NMDCEvents.eventGetNickList(u, data);
                break;
            case NMDCCommands.Key:
                NMDCEvents.eventKey(u, data);
                break;
            //case NMDCCommands.Kick:
            //case NMDCCommands.MultiConnectToMe:
            //case NMDCCommands.MultiSearch:
            case NMDCCommands.MyINFO:
                NMDCEvents.eventMyINFO(u, data);
                break;
            case NMDCCommands.MyPass:
                NMDCEvents.eventMyPass(u, data);
                break;
            //case NMDCCommands.OpForceMove:
            //case NMDCCommands.Quit:
            case NMDCCommands.RevConnectToMe:
                NMDCEvents.eventRevConnectToMe(u, data);
                break;
            case NMDCCommands.Search:
                NMDCEvents.eventSearch(u, data);
                break;
            case NMDCCommands.SR:
                NMDCEvents.eventSR(u, data);
                break;
            case NMDCCommands.Supports:
                NMDCEvents.eventSupports(u, data);
                break;
            case NMDCCommands.To:
                NMDCEvents.eventTo(u, data);
                break;
            //case NMDCCommands.UserIP:
            case NMDCCommands.ValidateNick:
                NMDCEvents.eventValidateNick(u, data);
                break;
            case NMDCCommands.Version:
                NMDCEvents.eventVersion(u, data);
                break;
            default:
                NMDCEvents.eventUnknow(u, data);
                break;
        }
    }
}
