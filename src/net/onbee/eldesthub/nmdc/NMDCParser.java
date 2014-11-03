package net.onbee.eldesthub.nmdc;

public class NMDCParser {

    public static String[] Parse(int type, String command) {
        try {
            switch (type) {
                case NMDCCommands.Chat:
                    return parseChat(command);
                case NMDCCommands.Close:
                    return parseClose(command);
                case NMDCCommands.ConnectToMe:
                    return parseConnectToMe(command);
                case NMDCCommands.GetINFO:
                    return parseGetINFO(command);
                case NMDCCommands.GetNickList:
                    return parseGetNickList(command);
                case NMDCCommands.Key:
                    return parseKey(command);
                case NMDCCommands.Kick:
                    return parseKick(command);
                case NMDCCommands.MultiConnectToMe:
                    return parseMultiConnectToMe(command);
                case NMDCCommands.MultiSearch:
                    return parseMultiSearch(command);
                case NMDCCommands.MyINFO:
                    return parseMyINFO(command);
                case NMDCCommands.MyPass:
                    return parseMyPass(command);
                case NMDCCommands.OpForceMove:
                    return parseOpForceMove(command);
                case NMDCCommands.Quit:
                    return parseQuit(command);
                case NMDCCommands.RevConnectToMe:
                    return parseRevConnectToMe(command);
                case NMDCCommands.Search:
                    return parseSearch(command);
                case NMDCCommands.SR:
                    return parseSR(command);
                case NMDCCommands.Supports:
                    return parseSupports(command);
                case NMDCCommands.To:
                    return parseTo(command);
                case NMDCCommands.UserIP:
                    return parseUserIP(command);
                case NMDCCommands.ValidateNick:
                    return parseValidateNick(command);
                case NMDCCommands.Version:
                    return parseVersion(command);
            }
            return null;
        } catch (StringIndexOutOfBoundsException ex) {
            return null;
        }
    }

    private static String[] parseChat(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.Chat, cmd) != 0) {
            String ret[] = new String[2];
            String tmp = cmd.substring(1);
            ret[0] = tmp.substring(0, tmp.indexOf(">"));
            tmp = tmp.substring(tmp.indexOf(">") + 2);
            ret[1] = tmp;
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseClose(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.Close, cmd) != 0) {
            String ret[] = new String[2];
            ret[0] = cmd.substring(6);
            String tmp = cmd.substring(7);
            ret[1] = tmp;
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseConnectToMe(String cmd) {
        int ch = NMDCCommands.checkCommand(NMDCCommands.Close, cmd);
        if (ch != 0) {
            if (ch == 1) {
                String ret[] = new String[4];
                ret[0] = cmd.substring(12);
                String tmp = cmd.substring(13);
                ret[1] = tmp.substring(0, tmp.indexOf(" "));
                tmp = tmp.substring(tmp.indexOf(" ") + 1);
                ret[2] = tmp.substring(0, tmp.indexOf(":"));
                tmp = tmp.substring(tmp.indexOf(":") + 1);
                ret[3] = tmp;
                return ret;
            } else {
                String ret[] = new String[5];
                ret[0] = cmd.substring(12);
                String tmp = cmd.substring(13);
                ret[1] = tmp.substring(0, tmp.indexOf(" "));
                tmp = tmp.substring(tmp.indexOf(" ") + 1);
                ret[2] = tmp.substring(0, tmp.indexOf(" "));
                tmp = tmp.substring(tmp.indexOf(" ") + 1);
                ret[3] = tmp.substring(0, tmp.indexOf(":"));
                tmp = tmp.substring(tmp.indexOf(":") + 1);
                ret[4] = tmp;
                return ret;
            }
        } else {
            return null;
        }

    }

    private static String[] parseGetINFO(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.GetINFO, cmd) != 0) {
            String ret[] = new String[3];
            ret[0] = cmd.substring(8);
            String tmp = cmd.substring(9);
            ret[1] = tmp.substring(0, tmp.indexOf(" "));
            tmp = tmp.substring(tmp.indexOf(" ") + 1);
            ret[2] = tmp;
            return ret;
        } else {
            return null;
        }

    }

    private static String[] parseGetNickList(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.GetNickList, cmd) != 0) {
            String ret[] = new String[1];
            ret[0] = cmd;
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseKey(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.Key, cmd) != 0) {
            String ret[] = new String[2];
            ret[0] = cmd.substring(4);
            String tmp = cmd.substring(5);
            ret[1] = tmp;
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseKick(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.Kick, cmd) != 0) {
            String ret[] = new String[2];
            ret[0] = cmd.substring(5);
            String tmp = cmd.substring(6);
            ret[1] = tmp;
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseMultiConnectToMe(String cmd) {
        return null;
    }

    private static String[] parseMultiSearch(String cmd) {
        return null;
    }

    private static String[] parseMyINFO(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.MyINFO, cmd) != 0) {
            String ret[] = new String[9];
            ret[0] = cmd.substring(7);
            String tmp = cmd.substring(8);
            ret[1] = tmp.substring(4);
            tmp = tmp.substring(5);
            ret[2] = tmp.substring(0, tmp.indexOf(" "));
            tmp = tmp.substring(tmp.indexOf(" ") + 1);
            ret[3] = tmp.substring(0, tmp.indexOf("<"));
            tmp = tmp.substring(tmp.indexOf("<") + 1);
            ret[4] = tmp.substring(0, tmp.indexOf(">"));
            tmp = tmp.substring(tmp.indexOf(">$ $") + 4);
            ret[5] = tmp.substring(0, tmp.indexOf("$") - 1);
            tmp = tmp.substring(tmp.indexOf("$") - 1);
            ret[6] = tmp.substring(0, tmp.indexOf("$"));
            tmp = tmp.substring(tmp.indexOf("$") + 1);
            ret[7] = tmp.substring(0, tmp.indexOf("$"));
            tmp = tmp.substring(tmp.indexOf("$") + 1);
            ret[8] = tmp.substring(0, tmp.indexOf("$"));
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseMyPass(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.MyPass, cmd) != 0) {
            String ret[] = new String[2];
            ret[0] = cmd.substring(7);
            String tmp = cmd.substring(8);
            ret[1] = tmp;
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseOpForceMove(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.OpForceMove, cmd) != 0) {
            String ret[] = new String[4];
            ret[0] = cmd.substring(12);
            String tmp = cmd.substring(19);
            ret[1] = tmp.substring(0, tmp.indexOf("$Where:"));
            tmp = tmp.substring(tmp.indexOf("$Where:") + 8);
            ret[2] = tmp.substring(0, tmp.indexOf("$Msg:"));
            tmp = tmp.substring(tmp.indexOf("$Msg:") + 6);
            ret[3] = tmp;
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseQuit(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.Quit, cmd) != 0) {
            String ret[] = new String[2];
            ret[0] = cmd.substring(5);
            String tmp = cmd.substring(6);
            ret[1] = tmp;
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseRevConnectToMe(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.RevConnectToMe, cmd) != 0) {
            String ret[] = new String[3];
            ret[0] = cmd.substring(15);
            String tmp = cmd.substring(16);
            ret[1] = tmp.substring(0, tmp.indexOf(" "));
            tmp = tmp.substring(tmp.indexOf(" ") + 1);
            ret[2] = tmp;
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseSearch(String cmd) {
        int ch = NMDCCommands.checkCommand(NMDCCommands.Search, cmd);
        if (ch != 0) {
            if (ch == 1) {
                String ret[] = new String[8];
                ret[0] = cmd.substring(7);
                String tmp = cmd.substring(8);
                ret[1] = tmp.substring(0, tmp.indexOf(":"));
                tmp = tmp.substring(tmp.indexOf(":") + 1);
                ret[2] = tmp.substring(0, tmp.indexOf(" "));
                tmp = tmp.substring(tmp.indexOf(" ") + 1);
                ret[3] = tmp.substring(0, tmp.indexOf("?"));
                tmp = tmp.substring(tmp.indexOf("?") + 1);
                ret[4] = tmp.substring(0, tmp.indexOf("?"));
                tmp = tmp.substring(tmp.indexOf("?") + 1);
                ret[5] = tmp.substring(0, tmp.indexOf("?"));
                tmp = tmp.substring(tmp.indexOf("?") + 1);
                ret[6] = tmp.substring(0, tmp.indexOf("?"));
                tmp = tmp.substring(tmp.indexOf("?") + 1);
                ret[7] = tmp;
                return ret;
            } else {
                String ret[] = new String[7];
                ret[0] = cmd.substring(7);
                String tmp = cmd.substring(12);
                ret[1] = tmp.substring(0, tmp.indexOf(" "));
                tmp = tmp.substring(tmp.indexOf(" ") + 1);
                ret[2] = tmp.substring(0, tmp.indexOf("?"));
                tmp = tmp.substring(tmp.indexOf("?") + 1);
                ret[3] = tmp.substring(0, tmp.indexOf("?"));
                tmp = tmp.substring(tmp.indexOf("?") + 1);
                ret[4] = tmp.substring(0, tmp.indexOf("?"));
                tmp = tmp.substring(tmp.indexOf("?") + 1);
                ret[5] = tmp.substring(0, tmp.indexOf("?"));
                tmp = tmp.substring(tmp.indexOf("?") + 1);
                ret[6] = tmp;
                return ret;
            }
        } else {
            return null;
        }
    }

    private static String[] parseSR(String cmd) {
        int ch = NMDCCommands.checkCommand(NMDCCommands.SR, cmd);
        if (ch != 0) {
            if (ch == 1) {
                String ret[] = new String[8];
                ret[0] = cmd.substring(3);
                String tmp = cmd.substring(4);
                ret[1] = tmp.substring(0, tmp.indexOf(" "));
                tmp = tmp.substring(tmp.indexOf(" ") + 1);
                ret[2] = tmp.substring(0, tmp.indexOf(0x05));
                tmp = tmp.substring(tmp.indexOf(0x05) + 1);
                ret[3] = tmp.substring(0, tmp.indexOf("/"));
                tmp = tmp.substring(tmp.indexOf("/") + 1);
                ret[4] = tmp.substring(0, tmp.indexOf(0x05));
                tmp = tmp.substring(tmp.indexOf(" ") + 1);
                ret[5] = tmp.substring(0, tmp.indexOf(" ("));
                tmp = tmp.substring(tmp.indexOf(" (") + 2);
                ret[6] = tmp.substring(0, tmp.indexOf(")"));
                tmp = tmp.substring(tmp.indexOf(")") + 2);
                ret[7] = tmp;
                return ret;
            } else {
                String ret[] = new String[7];
                ret[0] = cmd.substring(3);
                String tmp = cmd.substring(4);
                ret[1] = tmp.substring(0, tmp.indexOf(" "));
                tmp = tmp.substring(tmp.indexOf(" ") + 1);
                ret[2] = tmp.substring(0, tmp.indexOf(0x05));
                tmp = tmp.substring(tmp.indexOf(0x05) + 1);
                ret[3] = tmp.substring(0, tmp.indexOf("/"));
                tmp = tmp.substring(tmp.indexOf("/") + 1);
                ret[4] = tmp.substring(0, tmp.indexOf(0x05));
                tmp = tmp.substring(tmp.indexOf(" ") + 1);
                ret[5] = tmp.substring(0, tmp.indexOf(" ("));
                tmp = tmp.substring(tmp.indexOf(" (") + 2);
                ret[6] = tmp.substring(0, tmp.indexOf(")"));
                return ret;
            }
        } else {
            return null;
        }
    }

    private static String[] parseSupports(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.Supports, cmd) != 0) {
            int l = cmd.split("\\s").length;
            String ret[] = new String[l];
            ret[0] = cmd.substring(9);
            String tmp = cmd.substring(10);
            String[] tmp2 = tmp.split(" ");
            System.arraycopy(tmp2, 0, ret, 1, tmp2.length);
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseTo(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.To, cmd) != 0) {
            String ret[] = new String[5];
            ret[0] = cmd.substring(3);
            String tmp = cmd.substring(5);
            ret[1] = tmp.substring(0, tmp.indexOf(" "));
            tmp = tmp.substring(tmp.indexOf(" ") + 7);
            ret[2] = tmp.substring(0, tmp.indexOf(" "));
            tmp = tmp.substring(tmp.indexOf(" ") + 3);
            ret[3] = tmp.substring(0, tmp.indexOf(">"));
            tmp = tmp.substring(tmp.indexOf(">") + 2);
            ret[4] = tmp;
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseUserIP(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.UserIP, cmd) != 0) {
            int l = cmd.split("\\${2}").length;
            String ret[] = new String[l];
            ret[0] = cmd.substring(7);
            String tmp = cmd.substring(8);
            String[] tmp2 = tmp.split("\\${2}");
            System.arraycopy(tmp2, 0, ret, 1, tmp2.length);
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseValidateNick(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.ValidateNick, cmd) != 0) {
            String ret[] = new String[2];
            ret[0] = cmd.substring(13);
            String tmp = cmd.substring(14);
            ret[1] = tmp;
            return ret;
        } else {
            return null;
        }
    }

    private static String[] parseVersion(String cmd) {
        if (NMDCCommands.checkCommand(NMDCCommands.Version, cmd) != 0) {
            String ret[] = new String[2];
            ret[0] = cmd.substring(5);
            String tmp = cmd.substring(6);
            ret[1] = tmp;
            return ret;
        } else {
            return null;
        }
    }
}
