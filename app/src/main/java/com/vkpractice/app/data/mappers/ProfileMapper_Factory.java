// Generated by Dagger (https://dagger.dev).
package com.vkpractice.app.data.mappers;

import dagger.internal.Factory;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class ProfileMapper_Factory implements Factory<ProfileMapper> {
  @Override
  public ProfileMapper get() {
    return newInstance();
  }

  public static ProfileMapper_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ProfileMapper newInstance() {
    return new ProfileMapper();
  }

  private static final class InstanceHolder {
    private static final ProfileMapper_Factory INSTANCE = new ProfileMapper_Factory();
  }
}