package com.esc.bluespring.domain.friendship.exception;

import static com.esc.bluespring.domain.friendship.exception.FriendshipExceptionCode.FRIENDSHIP_NOT_FOUND_EXCEPTION;

import com.esc.bluespring.common.exception.ApplicationException;

public class FriendshipException extends ApplicationException {

    protected FriendshipException(FriendshipExceptionCode exceptionCode) {
        super(exceptionCode, null);
    }

    static public class FriendshipNotFoundException extends FriendshipException{
        public FriendshipNotFoundException() {
            super(FRIENDSHIP_NOT_FOUND_EXCEPTION);
        }
    }
}
