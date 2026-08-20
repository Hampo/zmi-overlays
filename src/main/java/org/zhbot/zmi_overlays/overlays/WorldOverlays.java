package org.zhbot.zmi_overlays.overlays;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.events.*;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import org.zhbot.zmi_overlays.ZMIOverlaysConfig;
import org.zhbot.zmi_overlays.ZMIOverlaysPlugin;
import org.zhbot.zmi_overlays.utils.GraphicsUtils;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WorldOverlays extends Overlay {
    private static final Set<Integer> ENEMY_IDS = ImmutableSet.of(
            NpcID.RC_ZMI_MELEE,
            NpcID.RC_ZMI_MELEE2,
            NpcID.RC_ZMI_RANGER,
            NpcID.RC_ZMI_RANGER2,
            NpcID.RC_ZMI_MAGE,
            NpcID.RC_ZMI_MAGE2,
            NpcID.RC_ZMI_LIZARD,
            NpcID.RC_ZMI_RUNERUNNER,
            NpcID.RC_ZMI_RUNERUNNER2
    );

    private final Client client;
    private final ClientThread clientThread;
    private final ZMIOverlaysPlugin plugin;
    private final ZMIOverlaysConfig config;
    private final GraphicsUtils graphicsUtils;

    private GameObject altar;
    private GameObject entrance;

    private NPC eniola;
    private final List<NPC> enemies = new ArrayList<>();

    @Inject
    public WorldOverlays(Client client, ClientThread clientThread, ZMIOverlaysPlugin plugin, ZMIOverlaysConfig config, GraphicsUtils graphicsUtils)
    {
        this.client = client;
        this.clientThread = clientThread;
        this.plugin = plugin;
        this.config = config;
        this.graphicsUtils = graphicsUtils;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    public void startup()
    {
        clientThread.invoke(() ->
        {
            if (client.getGameState() != GameState.LOGGED_IN || plugin.outsideOuraniaArea())
                return;

            var worldView = client.getTopLevelWorldView();
            if (worldView == null)
                return;

            for (var npc : worldView.npcs())
            {
                var id = npc.getId();

                if (id == NpcID.RC_ZMI_BANKER)
                    eniola = npc;
                else if (ENEMY_IDS.contains(id))
                    enemies.add(npc);
            }

            var scene = worldView.getScene();
            if (scene == null)
                return;

            var tiles = scene.getTiles()[worldView.getPlane()];
            for (var xTiles : tiles)
            {
                if (xTiles == null)
                    continue;

                for (var tile : xTiles)
                {
                    if (tile == null)
                        continue;

                    var gameObjects = tile.getGameObjects();
                    if (gameObjects == null)
                        continue;

                    for (var object : gameObjects)
                    {
                        switch (object.getId())
                        {
                            case ObjectID.RC_ZMI_DUNGEON_CRACKED_CENTER_ALTAR:
                                altar = object;
                                break;
                            case ObjectID.RC_ZMI_DUNGEON_ENTRANCE:
                                entrance = object;
                                break;
                        }
                    }
                }
            }
        });
    }

    public void cleanup()
    {
        altar = null;
        entrance = null;

        eniola = null;
        enemies.clear();
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (plugin.outsideOuraniaArea())
            return null;

        if (config.altarShow() && altar != null)
            graphicsUtils.renderObject(graphics, altar, config.altarRenderMode(), config.altarColour());

        if (config.entranceLadderShow() && entrance != null)
            graphicsUtils.renderObject(graphics, entrance, config.entranceLadderRenderMode(), config.entranceLadderColour());

        if (config.eniolaShow() && eniola != null)
            graphicsUtils.renderNPC(graphics, eniola, config.eniolaRenderMode(), config.eniolaColour());

        if (config.enemyShow())
            for (var enemy : enemies)
                graphicsUtils.renderNPC(graphics, enemy, config.enemyRenderMode(), config.enemyColour());

        return null;
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event)
    {
        var object = event.getGameObject();

        switch (object.getId())
        {
            case ObjectID.RC_ZMI_DUNGEON_CRACKED_CENTER_ALTAR:
                altar = object;
                break;
            case ObjectID.RC_ZMI_DUNGEON_ENTRANCE:
                entrance = object;
                break;
        }
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event)
    {
        switch (event.getGameObject().getId())
        {
            case ObjectID.RC_ZMI_DUNGEON_CRACKED_CENTER_ALTAR:
                altar = null;
                break;
            case ObjectID.RC_ZMI_DUNGEON_ENTRANCE:
                entrance = null;
                break;
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        switch (event.getGameState())
        {
            case HOPPING:
            case LOGGING_IN:
            case LOADING:
                cleanup();
                break;
            case LOGGED_IN:
                startup();
                break;
        }
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event)
    {
        var npc = event.getNpc();
        var id = npc.getId();

        if (id == NpcID.RC_ZMI_BANKER)
            eniola = npc;
        else if (ENEMY_IDS.contains(id))
            enemies.add(npc);
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event)
    {
        var npc = event.getNpc();
        var id = npc.getId();

        if (id == NpcID.RC_ZMI_BANKER)
            eniola = null;
        else if (ENEMY_IDS.contains(id))
            enemies.remove(npc);
    }
}
