package com.couchbase.connect.kafka.filter;

import com.couchbase.connect.kafka.handler.source.DocumentEvent;

public class OnlyMutationsFilter implements Filter {

    @Override
    public boolean pass(DocumentEvent event) {
        return event.isMutation();
    }

}
