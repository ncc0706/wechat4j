package org.hoh.wechat4j.cache;

import java.util.UUID; 

public abstract interface Cache<T extends BaseType>
{
  public abstract void set(String paramString, Object paramObject);
  
  public abstract T get(UUID paramUUID);
  
  public abstract T get(String paramString);
  
  public abstract void remove(UUID[] paramArrayOfUUID);
}
