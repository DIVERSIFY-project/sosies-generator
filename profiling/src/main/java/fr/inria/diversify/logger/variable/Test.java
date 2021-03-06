package fr.inria.diversify.logger.variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Simon on 15/01/15.
 */
public class Test {
    protected String signature;
    protected List<SingleMonitoringPoint> monitoringPoints;


    public Test(String signature) {
        this.signature = signature;
        monitoringPoints = new ArrayList<>();
    }

    public String getSignature() {
        return signature;
    }


    public void addAllMonitoringPoint(List<SingleMonitoringPoint> monitoringPoints) {
        this.monitoringPoints.addAll(monitoringPoints);
    }

    public int size() {
        return monitoringPoints.size();
    }

    public SingleMonitoringPoint geMonitoringPoint(String id) {
        return monitoringPoints.stream()
                .filter(m -> id.equals(m.getId()))
                .findFirst()
                .orElse(null);
    }

    public Set<String> getAllId() {
        return monitoringPoints.stream()
                .map(point -> point.getId())
                .collect(Collectors.toSet());
    }


    public Set<String> diff(Test other) {
        Set<String> ids = getAllId();
        ids.addAll(getAllId());

        return ids.stream()
                .filter(id -> {
                    SingleMonitoringPoint p1 = geMonitoringPoint(id);
                    SingleMonitoringPoint p2 = other.geMonitoringPoint(id);
                    if (p1 != null && p2 != null) {
                        return !p1.equals(p2);
                    } else {
                        return false;
                    }
//                    return p1 == null || p2 == null || !p1.equals(p2);
                })
                .collect(Collectors.toSet());
    }
}
