package tech.wesleystevens.WGU_MobileDev.Entities;

public enum AssessType {
    OBJECTIVE (0),
    PERFORMANCE (1);

    private final int type;
    AssessType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        if (this.type == 0) {
            return "Objective";
        } else {
            return "Performance";
        }
    }
}
