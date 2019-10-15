package org.kie.workbench.common.stunner.core.client.session.command.impl;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import org.kie.workbench.common.stunner.core.client.api.SessionManager;
import org.kie.workbench.common.stunner.core.client.session.ClientSession;
import org.kie.workbench.common.stunner.core.client.session.Session;

@Singleton
public class ClipboardCommandsProducer implements Serializable {

    private someString singleTonString = new someString("Singleton String");
    private static CopySelectionSessionCommand copySelectionCommand = null;
    private static ClientSession sessionX;


 //   @Produces
 //   @Default
 //   @Any
    public static CopySelectionSessionCommand getCopySelectionSessionCommand(final @New Event<CopySelectionSessionCommandExecutedEvent> commandExecutedEvent, final SessionManager session) {

        GWT.log("Getting Copy Command event class: " + commandExecutedEvent.getClass());
        GWT.log("Getting Copy Command event: " + commandExecutedEvent);
        GWT.log("Getting Copy Command event: " + commandExecutedEvent.hashCode());
        GWT.log("Getting Copy Command Session: " + session.getCurrentSession().getSessionUUID());

        boolean flag1 = false;
        boolean flag2 = false;

        flag1 = (copySelectionCommand == null && sessionX == null);
        GWT.log("New Copy Command Flag 1 : " + flag1);

        if (!flag1) {
            flag2 = (session.getCurrentSession() != sessionX);
            GWT.log("New Copy Command Flag 2 : " + flag2);
            GWT.log("this Command Session: " + sessionX.getSessionUUID());
        }


        if (true || (copySelectionCommand == null && sessionX == null) || (session.getCurrentSession().getSessionUUID() != sessionX.getSessionUUID())) {
            GWT.log("Triggering New Copy Command ");
            copySelectionCommand = new CopySelectionSessionCommand();
            sessionX = session.getCurrentSession();
           }

        return copySelectionCommand;
    }

    @Produces
    public someString getsomeString() {
        GWT.log("Getting New String");
        return singleTonString;
    }

    public class someString {
        String name;

        public someString(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
