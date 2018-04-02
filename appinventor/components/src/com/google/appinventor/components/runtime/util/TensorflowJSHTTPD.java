// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0
// This work is licensed under a Creative Commons Attribution 3.0 Unported License.

package com.google.appinventor.components.runtime.util;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.net.Socket;

import android.util.Log;

public class TensorflowJSHTTPD extends NanoHTTPD {

  private File rootDir;
  private Context context;
  private List<String> component_list;

  public TensorflowJSHTTPD(int port, File wwwroot, Context context) throws IOException {
    super(port, wwwroot);
    this.rootDir = wwwroot;
    this.context = context;
    this.component_list = Arrays.asList(context.getAssets().list("component"));
    Log.d("TensorflowJSHTTPD", Arrays.toString(context.getAssets().list("component")));
  }

  public Response serve(String uri, String method, Properties header, Properties params, Properties files, Socket mySocket) {
    try {
      String mimeType;
      InputStream inputStream;
      if (component_list.contains(uri.substring(1))) {
        if (uri.endsWith(".html")) {
          mimeType = "text/html";
        } else if (uri.endsWith(".js")) {
          mimeType = "application/javascript";
        } else if (uri.endsWith(".json")) {
          mimeType = "application/json";
        } else {
          mimeType = "application/octet-stream";
        }
        Log.d("TensorflowJSHTTD", uri + " with type " + mimeType);
        inputStream = context.getAssets().open("component" + uri);
        return new Response(HTTP_OK, mimeType, inputStream);
      } else {
        Log.d("TensorflowJSHTTPD", uri + " not found");
        return new Response(HTTP_NOTFOUND, MIME_PLAINTEXT, "404");
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
