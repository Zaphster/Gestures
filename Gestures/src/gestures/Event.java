/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestures;

import java.util.HashMap;

/**
 *
 * @author Cameron
 */
public class Event {
    private static Integer handlerID = 1;
    private static HashMap<Event.TYPE, HashMap> eventHandlers = new HashMap<>();
    private static HashMap<Integer, Event.TYPE> handlerIdToType = new HashMap<>();
    
    public enum TYPE{
        COMMAND_PERFORMED,
        GESTURE_CAPTURED,
        GESTURE_PERFORMED,
        GESTURE_SET_ACTIVE,
        GESTURE_SET_INACTIVE,
        GESTURE_DELETED,
        LEAP_CONTROLLER_CONNECTED,
        LEAP_CONTROLLER_DISCONNECTED,
        USER_SWITCHED,
        USER_CREATED,
        USER_DELETED,
        USER_LIST_CHANGED,
        USER_ADDED_GESTURE,
        USER_REMOVED_GESTURE,
        USER_MAPPED_GESTURE_TO_COMMAND,
        USER_REMOVED_GESTURE_FROM_COMMAND,
        GESTURES_TOO_SIMILAR,
        FIND_GESTURE_INITIATED_IN_DECISION_TREE,
    }
    
    private static Integer getNextID(){
        return handlerID++;
    }
   
    public static Integer registerHandler(Event.TYPE event, EventHandler handler){
        Integer id = Event.getNextID();
        if(!eventHandlers.containsKey(event)){
            HashMap<Integer, EventHandler> handlerHash = new HashMap<>();
            eventHandlers.put(event, handlerHash);
        }
        eventHandlers.get(event).put(id, handler);
        handlerIdToType.put(id, event);
        return id;
    }
    
    public static void removeHandler(Integer id){
        eventHandlers.get(handlerIdToType.get(id)).remove(id);
    }
    
    public static void trigger(Event event){
        if(eventHandlers.get(event.type) != null){
            eventHandlers.get(event.type).forEach((id, handler) -> {
                EventHandler _handler = (EventHandler)handler;
                _handler.handle(event);
            });
        } else {
            Debugger.print(event.type + " does not have any handlers");
        }
    }
    
    public final Event.TYPE type;
    private HashMap details = new HashMap<>();
    
    public Event(Event.TYPE type){
        this.type = type;
    }
    
    public void addDetail(String name, Object detail){
        details.put(name, detail);
    }
    
    public Object get(String name){
        return details.get(name);
    }
    
    public void trigger(){
        Event.trigger(this);
    }
}
