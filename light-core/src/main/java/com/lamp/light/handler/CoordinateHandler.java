/*
* frxs Inc.  湖南兴盛优选电子商务有限公司.
* Copyright (c) 2017-2019. All Rights Reserved.
*/
package com.lamp.light.handler;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.QueryStringEncoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.ErrorDataEncoderException;

public interface CoordinateHandler<T,V> {

    static final ThreadLocal<CoordinateHandlerWrapper> COORDINATEHANDLER = new ThreadLocal<CoordinateHandlerWrapper>() {

        public CoordinateHandlerWrapper initialValue() {
            return new CoordinateHandlerWrapper();
        }
    };

    public static CoordinateHandlerWrapper getCoordinateHandlerWrapper() {
        return COORDINATEHANDLER.get();
    }

    void handler(String key, V value);

    void clean();
    
    static abstract class AbstractCoordinateHandler<T,V> implements CoordinateHandler<T,V> {

        T object;

        void setObject(T object) {
            this.object = object;
        }
        
        public void clean() {
            this.object = null;
        }
    }

    static class CookieCoordinateHandler extends AbstractCoordinateHandler<HttpHeaders,String> {
        @Override
        public void handler(String name, String value) {
            object.add(name, value);
        }
    }

    static class HeaderCoordinateHandler extends AbstractCoordinateHandler<HttpHeaders,String> {
        @Override
        public void handler(String name, String value) {
            object.add(name, value);
        }
    }

    static class PathCoordinateHandler extends AbstractCoordinateHandler<String,String> {
        @Override
        public void handler(String name, String value) {

        }
    }

    static class QueryCoordinateHandler extends AbstractCoordinateHandler<QueryStringEncoder,String> {
        @Override
        public void handler(String name, String value) {
            object.addParam(name, value);
        }
    }

    static class FieldCoordinateHandler extends AbstractCoordinateHandler<HttpPostRequestEncoder,String> {
        @Override
        public void handler(String name, String value) {
            try {
                object.addBodyAttribute(name, value);
            } catch (ErrorDataEncoderException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    static class UploadCoordinateHandler extends AbstractCoordinateHandler<HttpPostRequestEncoder,Object> {
        @Override
        public void handler(String name, Object value) {
            try {
                object.addBodyAttribute(name, value.toString());
            } catch (ErrorDataEncoderException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static class CoordinateHandlerWrapper {

        public QueryCoordinateHandler queryCoordinateHandler = new QueryCoordinateHandler();

        public FieldCoordinateHandler fieldCoordinateHandler = new FieldCoordinateHandler();

        public PathCoordinateHandler pathCoordinateHandler = new PathCoordinateHandler();

        public HeaderCoordinateHandler headerCoordinateHandler = new HeaderCoordinateHandler();

        public CookieCoordinateHandler cookieCoordinateHandler = new CookieCoordinateHandler();
    }
}
