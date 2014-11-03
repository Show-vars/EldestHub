package net.onbee.eldesthub.usercommands;

public class Command {

    private int[] mprofile;
    private String mtitle;
    private int mtype;
    private int mcontext;
    private String mcommand;

    Command(int[] profile, int type, int context, String title, String cmd) {
        mprofile = profile;
        mtype = type;
        mcontext = context;
        mtitle = title;
        mcommand = cmd;
    }

    public boolean checkProfile(int p) {
        for(int i = 0; i < mprofile.length; i++) {
            if(mprofile[i] == p) {
                return true;
            }
        }
        return false;
    }

    public String build() {
        if (mtype == 255 || mtype == 0) {
            return "$UserCommand " + mtype + " " + mcontext;
        } else {
            return "$UserCommand " + mtype + " " + mcontext + " " + mtitle + "$" + mcommand + "&#124;";
        }
    }
}
