package nuparu.sevendaystomine.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.realmsclient.util.JsonUtils;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.loot.LootContext;
import net.minecraft.server.management.PlayerList;
import net.minecraft.stats.ServerStatisticsManager;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.CommonConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class UpgradeBlockTrigger implements ICriterionTrigger {

    private final Map<PlayerAdvancements, Set<Listener>> players = Maps.newIdentityHashMap();

    ResourceLocation resourceLocation;

    public UpgradeBlockTrigger(ResourceLocation resourceLocation){
        this.resourceLocation = resourceLocation;
    }

    @Override
    public ResourceLocation getId() {
        return resourceLocation;
    }

    public final void addPlayerListener(PlayerAdvancements p_192165_1_, Listener p_192165_2_) {
        this.players.computeIfAbsent(p_192165_1_, (p_227072_0_) -> {
            return Sets.newHashSet();
        }).add(p_192165_2_);
    }

    public final void removePlayerListener(PlayerAdvancements p_192164_1_, Listener p_192164_2_) {
        Set<Listener> set = this.players.get(p_192164_1_);
        if (set != null) {
            set.remove(p_192164_2_);
            if (set.isEmpty()) {
                this.players.remove(p_192164_1_);
            }
        }

    }

    @Override
    public void removePlayerListeners(PlayerAdvancements p_192167_1_) {
        this.players.remove(p_192167_1_);
    }

    protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate p_230241_2_, ConditionArrayParser p_230241_3_){
        String block = "";

        if (json.has("block")) {
            block = JsonUtils.getStringOr( "block",json,"");
        } else {
            throw new JsonSyntaxException("Expected property <block> for " + getId().toString());
        }

        return new Instance(resourceLocation,p_230241_2_,block);
    }

    @Override
    public ICriterionInstance createInstance(JsonObject p_230307_1_, ConditionArrayParser p_230307_2_) {
        EntityPredicate.AndPredicate entitypredicate$andpredicate = EntityPredicate.AndPredicate.fromJson(p_230307_1_, "player", p_230307_2_);
        return this.createInstance(p_230307_1_, entitypredicate$andpredicate, p_230307_2_);
    }

    protected void trigger(ServerPlayerEntity player, Predicate p_235959_2_, BlockState state) {
        PlayerAdvancements playeradvancements = player.getAdvancements();
        PlayerList playerList = player.server.getPlayerList();
        ServerStatisticsManager statsManager = playerList.getPlayerStats(player);


        Set<Listener> set = this.players.get(playeradvancements);
        if (set != null && !set.isEmpty()) {
            LootContext lootcontext = EntityPredicate.createContext(player, player);
            List<Listener> list = null;

            for(Listener listener : set) {
                Instance t = (Instance) listener.getTriggerInstance();
                if(!state.getBlock().getRegistryName().toString().equals(t.block)) return;;
                if (t.getPlayerPredicate().matches(lootcontext) && p_235959_2_.test(t)) {
                    if (list == null) {
                        list = Lists.newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null) {
                for(Listener listener1 : list) {
                    listener1.run(playeradvancements);
                }
            }

        }
    }

    public static class Instance implements ICriterionInstance {
        private final ResourceLocation criterion;
        private final String block;
        private final EntityPredicate.AndPredicate player;

        public Instance(ResourceLocation p_i231464_1_, EntityPredicate.AndPredicate p_i231464_2_, String block) {
            this.criterion = p_i231464_1_;
            this.player = p_i231464_2_;
            this.block = block;
        }

        public ResourceLocation getCriterion() {
            return this.criterion;
        }

        protected EntityPredicate.AndPredicate getPlayerPredicate() {
            return this.player;
        }

        public JsonObject serializeToJson(ConditionArraySerializer p_230240_1_) {
            JsonObject jsonobject = new JsonObject();
            jsonobject.add("player", this.player.toJson(p_230240_1_));
            return jsonobject;
        }

        public String toString() {
            return "AbstractCriterionInstance{criterion=" + this.criterion + '}';
        }
    }
}