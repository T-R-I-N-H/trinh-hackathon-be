package trinh_be.modules.diagram.utils;

import lombok.experimental.UtilityClass;
import trinh_be.modules.diagram.model.Diagram;
import trinh_be.modules.user.model.User;

@UtilityClass
public class DiagramUtils {
    public static boolean validDiagram(User user, Diagram diagram) {
        if (diagram == null) {
            return false;
        }
        return diagram.getOwnerEmail().equals(user.getEmail());
    }
}
