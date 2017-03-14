package cane.brothers.rpc.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import cane.brothers.rpc.data.TaskEntry;

public interface RpcTaskRepository extends JpaRepository<TaskEntry, Integer> {

    Set<TaskEntry> findByGroup(String groupName);
}
