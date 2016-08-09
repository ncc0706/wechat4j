package org.hoh.wechat4j.cache.impl;

import org.hoh.wechat4j.cache.EhcacheHandler;
import org.hoh.wechat4j.token.AccessTokenProxy;

public class TicketCache
{
  private static class SingletonHolder
  {
    private static TicketCache instance = new TicketCache();
  }
  
  public static final TicketCache getInstance()
  {
    return SingletonHolder.instance;
  }
  
  public void set(String key, Object value)
  {
    if (value == null) {
      return;
    }
    EhcacheHandler.getInstance().set("expiresCache", key, value);
  }
  
  public void remove(String key)
  {
    EhcacheHandler.getInstance().delete("expiresCache", key);
  }
  
  public String getTicket()
    throws Exception
  {
    Object object = EhcacheHandler.getInstance().get("expiresCache", "ticket");
    if (object != null)
    {
      if ((object instanceof String)) {
        return (String)object;
      }
      return null;
    }
    String ticket = AccessTokenProxy.getTicket();
    if (ticket != null) {
      EhcacheHandler.getInstance().set("expiresCache", "ticket", ticket);
    }
    return ticket;
  }
}
