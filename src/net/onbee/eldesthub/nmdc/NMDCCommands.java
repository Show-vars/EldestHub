package net.onbee.eldesthub.nmdc;

public class NMDCCommands {

    public static final int Close = 0;
    public static final int ConnectToMe = 1;
    public static final int GetINFO = 2;
    public static final int GetNickList = 3;
    public static final int Key = 4;
    public static final int Kick = 5;
    public static final int MyINFO = 6;
    public static final int MyPass = 7;
    public static final int MultiConnectToMe = 8;
    public static final int MultiSearch = 9;
    public static final int OpForceMove = 10;
    public static final int Quit = 11;
    public static final int RevConnectToMe = 12;
    public static final int Search = 13;
    public static final int SR = 14;
    public static final int Supports = 15;
    public static final int To = 16;
    public static final int UserIP = 17;
    public static final int Version = 18;
    public static final int ValidateNick = 19;
    public static final int Chat = 20;
    private static String regexip = "(([2]([0-4][0-9]|[5][0-5])|[0-1]?[0-9]?[0-9])[.]){3}(([2]([0-4][0-9]|[5][0-5])|[0-1]?[0-9]?[0-9]))";

    public static int checkCommand(int type, String command) {
        if (!command.endsWith("|")) {
            command += "|";
        }
        switch (type) {
            case NMDCCommands.Chat: {
                if (command.matches("^\\<\\S*\\>[\\s\\S]*?\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.Close: {
                if (command.matches("^\\$Close\\s\\S*\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.ConnectToMe: {
                if (command.matches("^\\$ConnectToMe\\s\\S*\\s" + regexip + "\\:\\S*\\|")) {
                    return 1;
                } else if (command.matches("^\\$ConnectToMe\\s\\S*\\s\\S*\\s" + regexip + "\\:\\S*\\|")) {
                    return 2;
                }
            }
            case NMDCCommands.GetINFO: {
                if (command.matches("^\\$GetINFO\\s\\S*\\s\\S*\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.GetNickList: {
                if (command.matches("^\\$GetNickList\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.Key: {
                if (command.matches("^\\$Key\\s.*\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.Kick: {
                if (command.matches("^\\$Kick\\s\\S*\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.MultiConnectToMe: {
                //if(command.matches("")) {
                return 0;
                //}
            }
            case NMDCCommands.MultiSearch: {
                //if(command.matches("")) {
                return 0;
                //}
            }
            case NMDCCommands.MyINFO: {
                if (command.matches("^\\$MyINFO\\s\\$ALL\\s\\S*\\s.*\\<.*\\>\\$\\s\\$.*\\$\\d*\\$\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.MyPass: {
                if (command.matches("^\\$MyPass\\s.*\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.OpForceMove: {
                if (command.matches("^\\$OpForceMove\\s\\$Who\\:\\s\\S*\\$Where\\:\\s\\S*\\$Msg\\:\\s.*\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.Quit: {
                if (command.matches("^\\$Quit\\s\\S*\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.RevConnectToMe: {
                if (command.matches("^\\$RevConnectToMe\\s\\S*\\s\\S*\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.Search: {
                if (command.matches("^\\$Search\\s" + regexip + "\\:\\d*\\s\\S*\\|")) {
                    return 1;
                } else if (command.matches("^\\$Search\\sHub\\:\\S*\\s\\S*\\|")) {
                    return 2;
                }
            }
            //$SR {ник_ответчика} {результат}{0x05}{свободные_слоты}/{всего_слотов}{0x05}{имя_хаба} ({ip_хаба:порт})[{0x05}{целевой_ник}]|
            case NMDCCommands.SR: {
                //if(command.matches("^\\$SR\\s\\S*\\s.*\\x05.*\\s\\d*\\/\\d*\\x05.*\\s\\(" + regexip + "\\)\\x05\\S*\\|")) {
                return 1;
                //} else if(command.matches("^\\$SR\\s\\S*\\s.*\\x05.*\\s\\d*\\/\\d*\\x05.*\\s\\(" + regexip + "\\)\\|")) {
                //    return 2;
                //}
            }
            case NMDCCommands.Supports: {
                if (command.matches("^\\$Supports(\\s\\S*)*\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.To: {
                if (command.matches("^\\$To\\:\\s\\S*\\sFrom\\:\\s\\S*\\s\\$\\<\\S*\\>[\\s\\S]*?\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.UserIP: {
                if (command.matches("^\\$UserIP\\s(\\S*\\${2})*\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.ValidateNick: {
                if (command.matches("^\\$ValidateNick\\s\\S*\\|")) {
                    return 1;
                }
            }
            case NMDCCommands.Version: {
                if (command.matches("^\\$Version\\s\\S*\\|")) {
                    return 1;
                }
            }
            defaut:
            {
                return 0;
            }
        }
        return 0;

    }

    public static int getType(String command) {
        if (command.startsWith("<")) {
            return NMDCCommands.Chat;
        } else if (command.startsWith("$Close")) {
            return NMDCCommands.Close;
        } else if (command.startsWith("$ConnectToMe")) {
            return NMDCCommands.ConnectToMe;
        } else if (command.startsWith("$GetINFO")) {
            return NMDCCommands.GetINFO;
        } else if (command.startsWith("$GetNickList")) {
            return NMDCCommands.GetNickList;
        } else if (command.startsWith("$Key")) {
            return NMDCCommands.Key;
        } else if (command.startsWith("$Kick")) {
            return NMDCCommands.Kick;
        } else if (command.startsWith("$MultiConnectToMe")) {
            return NMDCCommands.MultiConnectToMe;
        } else if (command.startsWith("$MultiSearch")) {
            return NMDCCommands.MultiSearch;
        } else if (command.startsWith("$MyINFO")) {
            return NMDCCommands.MyINFO;
        } else if (command.startsWith("$MyPass")) {
            return NMDCCommands.MyPass;
        } else if (command.startsWith("$OpForceMove")) {
            return NMDCCommands.OpForceMove;
        } else if (command.startsWith("$Quit")) {
            return NMDCCommands.Quit;
        } else if (command.startsWith("$RevConnectToMe")) {
            return NMDCCommands.RevConnectToMe;
        } else if (command.startsWith("$Search")) {
            return NMDCCommands.Search;
        } else if (command.startsWith("$SR")) {
            return NMDCCommands.SR;
        } else if (command.startsWith("$Supports")) {
            return NMDCCommands.Supports;
        } else if (command.startsWith("$To")) {
            return NMDCCommands.To;
        } else if (command.startsWith("$UserIP")) {
            return NMDCCommands.UserIP;
        } else if (command.startsWith("$ValidateNick")) {
            return NMDCCommands.ValidateNick;
        } else if (command.startsWith("$Version")) {
            return NMDCCommands.Version;
        } else {
            return -1;
        }
    }
}
