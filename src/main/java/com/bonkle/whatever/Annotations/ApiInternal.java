package com.bonkle.whatever.Annotations;

import java.lang.annotation.Documented;

@Documented
/**
 * This annotation is used to mark a class as internal to the API, and methods annotated with this as such should not be used.
 */
public @interface ApiInternal {  }
