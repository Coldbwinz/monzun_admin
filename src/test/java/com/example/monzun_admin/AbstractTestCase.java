package com.example.monzun_admin;

import com.github.javafaker.Faker;
import junit.framework.TestCase;

public abstract class AbstractTestCase extends TestCase {
    public static final Faker faker = new Faker();
}
