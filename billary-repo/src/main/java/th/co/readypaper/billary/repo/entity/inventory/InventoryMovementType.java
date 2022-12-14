package th.co.readypaper.billary.repo.entity.inventory;

import lombok.Getter;

public enum InventoryMovementType {
    ADD(1), REMOVE(2);

    @Getter
    private final int val;

    private InventoryMovementType(int val) {
        this.val = val;
    }

}
