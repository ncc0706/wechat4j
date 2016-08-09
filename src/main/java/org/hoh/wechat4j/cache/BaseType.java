package org.hoh.wechat4j.cache;

import java.io.Serializable;
import java.util.UUID;

import org.w3c.dom.Document;

public abstract interface BaseType
  extends Serializable
{
  public abstract UUID getId();
  
  public abstract BaseType clone();
  
  public abstract Document toXMLDocument()
    throws Exception;
  
  public abstract String toXMLString()
    throws Exception;
}
