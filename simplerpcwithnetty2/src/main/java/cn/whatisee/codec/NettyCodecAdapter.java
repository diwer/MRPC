package cn.whatisee.codec;

import cn.whatisee.common.spi.ExtensionLoader;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by ming on 2017/5/2.
 */
public class NettyCodecAdapter implements NettyCodecHander {

    private Codec codec;

    public NettyCodecAdapter() {
        codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension("simple");
    }

    public Object decode(ByteBuf inBuf) throws IOException {
            return codec.decode(inBuf);

    }

    public void encode(Object message, ByteBuf outBuf) throws IOException {

            codec.encode(message, outBuf);
    }

}
