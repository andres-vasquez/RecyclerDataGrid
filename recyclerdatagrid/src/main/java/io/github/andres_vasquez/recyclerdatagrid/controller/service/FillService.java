package io.github.andres_vasquez.recyclerdatagrid.controller.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by andresvasquez on 10/2/17.
 */

public class FillService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
