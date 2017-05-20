package mavonie.subterminal.unit.Skydive;

import android.util.Pair;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Models.Skydive.Tunnel;
import mavonie.subterminal.Models.Skydive.TunnelSession;
import mavonie.subterminal.unit.Base.BaseDBUnit;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tunnel unit tests
 */
public class TunnelTest extends BaseDBUnit {

    @Test
    public void saveToDb() {

        Tunnel tunnel = createTunnel();
        assertTrue(tunnel.exists());

        Tunnel dbTunnel = (Tunnel) new Tunnel().getOneById(tunnel.getId());

        Assert.assertTrue(tunnel.equals(dbTunnel));

        Tunnel tunnel1 = (Tunnel) new Tunnel().getItem(new Pair<>(Model._ID, Integer.toString(dbTunnel.getId())));
        assertEquals(tunnel1, dbTunnel);

        HashMap<String, Object> params = new HashMap<>();
        params.put(Model.FILTER_ORDER_DIR, Model.FILTER_ORDER_DIR_ASC);
        params.put(Model.FILTER_ORDER_FIELD, Dropzone.COLUMN_NAME_NAME);

        List tunnels = new Tunnel().getItems(params);

        assertNotNull(tunnels);
    }


    public static Tunnel createTunnel() {
        Tunnel tunnel = new Tunnel();

        tunnel.setCountry("Ireland");
        tunnel.setName("Wind Tunnel");
        tunnel.setDescription("Description");
        tunnel.setEmail("info@tunnel.com");
        tunnel.setPhone("0023466423");
        tunnel.setWebsite("http://tunnel.com");
        tunnel.setLatitude(53.2506);
        tunnel.setLongtitude(-7.1174);
        tunnel.setTunnelHeight(10.04);
        tunnel.setTunnelDiameter(4);

        tunnel.save();

        return tunnel;
    }

    public static TunnelSession createSession() {
        TunnelSession session = new TunnelSession();

        session.setTunnelId(createTunnel().getId());
        session.setLength(12);
        session.setDescription("Session description");
        session.setDate("2000-01-01");

        session.save();

        return session;
    }

    @Test
    public void testSetFeatured() {
        Tunnel tunnel = createTunnel();
        tunnel.setFeatured(Dropzone.FEATURED_TRUE);
        tunnel.save();

        Tunnel tunnelDB = (Tunnel) new Tunnel().getOneById(tunnel.getId());

        Assert.assertTrue(tunnelDB.equals(tunnel));
        Assert.assertEquals(tunnelDB.getFeatured(), (Integer) Dropzone.FEATURED_TRUE);
    }

    @Test
    public void testCreateSession() {
        TunnelSession session = createSession();

        assertNotNull(session.getId());
        assertTrue(session.exists());

        TunnelSession dbSession = (TunnelSession) new TunnelSession().getOneById(session.getId());
        assertEquals(dbSession, session);

        assertEquals(dbSession.getTunnel(), session.getTunnel());
    }
}
