package com.solidstategroup.radar.web.panels;

import com.solidstategroup.radar.model.Diagnosis;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

public class PatientPathologyPanel extends Panel {

    public PatientPathologyPanel(String id) {
        super(id);
        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        Form<Diagnosis> form = new Form<Diagnosis>("form");
        add(form);
    }
}
