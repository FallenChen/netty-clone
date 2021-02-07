package org.garry.netty.util;

public class ConvertUtil {

    public static int toInt(Object value)
    {
        if(value instanceof Number)
        {
            return ((Number) value).intValue();
        }else {
            return Integer.parseInt(String.valueOf(value));
        }
    }


    public static boolean toBoolean(Object value)
    {
        if(value instanceof Boolean)
        {
            return ((Boolean) value).booleanValue();
        }
        if(value instanceof Number)
        {
            return ((Number) value).intValue() !=0;
        }
        else
        {
            String s = String.valueOf(value);
            if (s.length() == 0)
            {
                return false;
            }
            try {
                return Integer.parseInt(s) != 0;
            }catch (NumberFormatException e)
            {

            }
            // todo 会执行吗
            switch (Character.toUpperCase(s.charAt(0)))
            {
                case 'T':
                case 'Y':
                    return true;
            }
            return false;
        }
    }
}
