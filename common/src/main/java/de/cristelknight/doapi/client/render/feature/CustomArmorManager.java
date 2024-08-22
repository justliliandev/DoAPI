package de.cristelknight.doapi.client.render.feature;

import com.google.common.collect.Sets;
import de.cristelknight.doapi.DoApi;
import de.cristelknight.doapi.Util;
import de.cristelknight.doapi.api.DoApiAPI;
import de.cristelknight.doapi.api.DoApiPlugin;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class CustomArmorManager<T extends LivingEntity> {
    private final Set<CustomArmorSet<T>> ARMORS = Sets.newHashSet();

    private final EntityModelSet modelLoader;

    public CustomArmorManager(EntityModelSet modelLoader) {
        this.modelLoader = modelLoader;
    }

    public boolean isEmpty() {
        return this.ARMORS.isEmpty();
    }

    public CustomArmorSet<T> addArmor(CustomArmorSet<T> model) {
        DoApi.LOGGER.info("Adding armor " + model.toString());
        this.ARMORS.add(model);
        return model;
    }

    public @Nullable CustomArmorSet<T> getSet(Item item) {
        for (CustomArmorSet<T> armor : this.ARMORS) {
            DoApi.LOGGER.info("Checking armor set model " + armor.toString());
            if (armor.getArmor().contains(item)) {
                DoApi.LOGGER.info("Found armor set model " + armor.toString());
                return armor;
            }
        }
        DoApi.LOGGER.info("No armor set model found");
        return null;
    }

    public @Nullable EntityModel<T> getModel(Item item, EquipmentSlot slot) {
        if (this.ARMORS.isEmpty()) {
            DoApi.LOGGER.info("No armor models found");
            this.updateArmors();
        }
        for (CustomArmorSet<T> armor : this.ARMORS) {
            DoApi.LOGGER.info("Checking armor model " + armor.toString());
            if (armor.getArmor().contains(item)) {
                DoApi.LOGGER.info("Found armor model " + armor.toString());
                return armor.getModel(slot);
            }
        }
        DoApi.LOGGER.info("No armor model found");
        return null;
    }

    public @Nullable ResourceLocation getTexture(Item item, String string) {
        if (this.ARMORS.isEmpty()) {
            DoApi.LOGGER.info("No armor textures found");
            this.updateArmors();
        }
        for (CustomArmorSet<T> armor : this.ARMORS) {
            DoApi.LOGGER.info("Checking armor texture " + armor.toString());
            if (armor.getArmor().contains(item)) {
                DoApi.LOGGER.info("Found armor texture " + armor.toString());
                return armor.getTexture(string);
            }
        }
        DoApi.LOGGER.info("No armor texture found");
        return null;
    }

    private void updateArmors() {
        List<DoApiAPI> apis = Util.getApis(DoApiAPI.class, "doapi", DoApiPlugin.class);
        for (DoApiAPI api : apis) {
            DoApi.LOGGER.info("Registering armor models" + api.getClass().getPackage());
            api.registerArmor(this, this.modelLoader);
        }
    }
}
