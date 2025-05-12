package ru.nataliaoskina.di;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import ru.nataliaoskina.handlers.StartCommandUpdateHandler;
import ru.nataliaoskina.handlers.api.UpdateHandler;
import ru.nataliaoskina.handlers.CallbackUpdateHandler;

import javax.inject.Singleton;

@Module
public abstract class BindingsModule {

    @Binds
    @Singleton
    public abstract UpdateHandler bindUpdateHandler(CallbackUpdateHandler impl);

    @Binds
    @IntoSet
    public abstract UpdateHandler bindCallbackHandler(CallbackUpdateHandler impl);

    @Binds
    @IntoSet
    public abstract UpdateHandler bindStartHandler(StartCommandUpdateHandler impl);

}
