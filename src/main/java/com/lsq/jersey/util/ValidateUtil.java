/*
 * Copyright (C) 2018 Pingan, Inc. All Rights Reserved.
 */
package com.lsq.jersey.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by LUSHUQIN443 on 2018/4/28.
 */
public class ValidateUtil {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> List<String> validate(T t) {
        if (t == null) return null;
        List<String> messageList = new LinkedList<String>();
        Set<ConstraintViolation<T>> set = validator.validate(t);
        for (ConstraintViolation<T> constraintViolation : set) {
            messageList.add(constraintViolation.getMessage());
        }
        return messageList;
    }
}
