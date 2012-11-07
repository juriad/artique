package cz.artique.server.service;

import java.util.List;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Link;

import cz.artique.client.service.Test1Service;
import cz.artique.server.meta.item.ItemMeta;
import cz.artique.shared.model.item.Item;
import cz.artique.shared.model.source.Source;
import cz.artique.utils.SourceException;

public class Test1ServiceImpl implements Test1Service {

    public Source addSource(String url) {
        SourceService ss = new SourceService();
        Link l = ss.getLink(url);
        if (l == null) {
            throw new SourceException("invalid link");
        }
        Source source = null;
        source = ss.getSource(l);
        if (source == null) {
            source = ss.createSource(l, SourceType.XML_SOURCE);
        }

        source.setUsage(1);
        Datastore.put(source);

        return source;
    }

    public List<Item> getItems() {
        ItemMeta meta = ItemMeta.get();
        List<Item> items = Datastore.query(meta).sort(meta.added.desc).asList();
        return items;
    }

}
