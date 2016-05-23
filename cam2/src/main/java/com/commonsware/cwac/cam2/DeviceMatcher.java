/***
 Copyright (c) 2016 CommonsWare, LLC

 Licensed under the Apache License, Version 2.0 (the "License"); you may
 not use this file except in compliance with the License. You may obtain
 a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.commonsware.cwac.cam2;

import android.os.Build;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class DeviceMatcher {
  private static final ArrayList<DeviceMatcher> CAMERA2_WHITELIST=
    new ArrayList<>();
  private final Pattern manufacturer;
  private final Pattern product;
  private final Pattern model;

  static {
    addToCameraTwoWhitelist(new Builder()
      .manufacturer("LGE")
      .product("hammerhead")
      .build());
  }

  public static void addToCameraTwoWhitelist(DeviceMatcher matcher) {
    CAMERA2_WHITELIST.add(matcher);
  }

  public static boolean supportsCameraTwo() {
    for (DeviceMatcher m : CAMERA2_WHITELIST) {
      if (m.isMatch()) {
        return(true);
      }
    }

    return(false);
  }

  private DeviceMatcher(Pattern manufacturer,
                       Pattern product, Pattern model) {
    this.manufacturer=manufacturer;
    this.product=product;
    this.model=model;
  }

  public boolean isMatch() {
    boolean result=true;

    if (manufacturer!=null) {
      result=manufacturer.matcher(Build.MANUFACTURER).matches();
    }

    if (result && product!=null) {
      result=product.matcher(Build.PRODUCT).matches();
    }

    if (result && model!=null) {
      result=model.matcher(Build.MODEL).matches();
    }

    return(result);
  }

  public static class Builder {
    private Pattern manufacturer;
    private Pattern product;
    private Pattern model;

    public Builder manufacturer(String mfr) {
      return(manufacturer(Pattern.compile(mfr)));
    }

    public Builder manufacturer(Pattern manufacturer) {
      this.manufacturer=manufacturer;

      return(this);
    }

    public Builder product(String product) {
      return(product(Pattern.compile(product)));
    }

    public Builder product(Pattern product) {
      this.product=product;

      return(this);
    }

    public Builder model(String model) {
      return(model(Pattern.compile(model)));
    }

    public Builder model(Pattern model) {
      this.model=model;

      return(this);
    }

    public DeviceMatcher build() {
      return(new DeviceMatcher(manufacturer, product, model));
    }
  }
}
