package com.skill_mentor.root.exception;

public class ClassRoomException extends RuntimeException{
    public ClassRoomException(String message, Throwable throwable){
        super(message, throwable);
    }

    public ClassRoomException(String message){
        super(message);
    }

    public ClassRoomException(){}
}

