package com.esc.bluespring.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class CustomSlice<T> {

    @JsonIgnore
    private Slice<T> slice;

    public CustomSlice(Slice<T> slice) {
        this.slice = slice;
    }

    public List<T> getContent() {
        return slice.getContent();
    }

    public boolean getHasNext() {
        return slice.hasNext();
    }

    public boolean getHasPrevious() {
        return slice.hasPrevious();
    }
}