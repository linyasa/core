package com.dotmarketing.business;

import com.dotmarketing.beans.Host;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.util.UUIDGenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class IdentifierAPITest {
    
    protected static final String id404 = "$$__404__CACHE_MISS__$$";
    final IdentifierAPI api = APILocator.getIdentifierAPI();
    final IdentifierCache cache = CacheLocator.getIdentifierCache();
    
    @Test
    public void testing404() throws Exception {
        
        final Host syshost = APILocator.getHostAPI().findSystemHost();
        
        // fake not yet created id and asset
        String fakeId=UUIDGenerator.generateUuid();
        Contentlet fakeCont=new Contentlet();
        fakeCont.setInode(UUIDGenerator.generateUuid());
        fakeCont.setStructureInode(CacheLocator.getContentTypeCache().getStructureByVelocityVarName("Host").getInode());
        
        // if we find a fake id it should end up in 404 cache
        api.find(fakeId);
        assertEquals(id404, cache.getIdentifier(fakeId).getAssetType());
        api.find(syshost, "/content."+fakeCont.getInode());
        assertEquals(id404, cache.getIdentifier(syshost.getIdentifier(),"/content."+fakeCont.getInode()).getAssetType());
        
        // now if we create an asset with that ID it should be cleared
        api.createNew(fakeCont, syshost, fakeId);
        assertNull(cache.getIdentifier(fakeId));
        assertNull(cache.getIdentifier(syshost.getIdentifier(), "/content."+fakeCont.getInode()));
        
        // this should load the identifier in both cache entries (by url and by id)
        api.find(fakeId);
        assertEquals(fakeId, cache.getIdentifier(fakeId).getId());
        assertEquals(fakeId, cache.getIdentifier(syshost.getIdentifier(), "/content."+fakeCont.getInode()).getId());
        
        
    }
}
