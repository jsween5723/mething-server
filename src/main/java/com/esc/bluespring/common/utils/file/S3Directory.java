package com.esc.bluespring.common.utils.file;

import lombok.Getter;

@Getter
public enum S3Directory implements Directory{

  MEMBER("member/"),
  ;

  private final String dirName;

  S3Directory(String dirName) {
    this.dirName = dirName;
  }

}
