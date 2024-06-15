package com.cargologix.library_mate.misc;

public class MemberInfo {
    private static String memberId;
    private static boolean login;

    public static boolean getLoginStatus() {
        return login ;
    }

    public static void setLogin(boolean login) {
        MemberInfo.login = login;
    }

    public static String getMemberId() {
        return memberId;
    }

    public static void setMemberId(String memberId) {
        MemberInfo.memberId = memberId;
    }
}
