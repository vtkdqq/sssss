package com.szzjcs.commons.util;

import org.slf4j.Logger;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.szzjcs.commons.logutil.LogProxy;

/**
 * @author: smartlv
 * @date 2014年2月17日 下午6:51:21
 */
public class SerializeUtil<T>
{
    private static Logger log = LogProxy.getLogger(SerializeUtil.class);

    private static final ThreadLocal<Kryo> localKryo = new ThreadLocal<Kryo>()
    {
        @Override
        protected Kryo initialValue()
        {
            return new Kryo();
        }
    };

    public static byte[] serialize(Object t)
    {
        Output output = null;
        try
        {
            byte[] buffer = new byte[1024 * 3];
            output = new Output(buffer);

            localKryo.get().writeClassAndObject(output, t);
            return output.toBytes();
        }
        catch (Throwable e)
        {
            log.error("serialize error on " + t.getClass().getName(), e);
        }
        finally
        {
            if (output != null)
            {
                output.close();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes)
    {
        Input input = null;
        try
        {
            input = new Input(bytes);
            return (T) localKryo.get().readClassAndObject(input);
        }
        catch (Throwable e)
        {
            log.error("deserialize error", e);
        }
        finally
        {
            if (input != null)
            {
                input.close();
            }
        }
        return null;
    }

}
