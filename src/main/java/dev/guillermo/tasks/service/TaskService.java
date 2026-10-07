package dev.guillermo.tasks.service;

import dev.guillermo.tasks.dto.TaskRequest;
import dev.guillermo.tasks.model.Task;
import dev.guillermo.tasks.model.TaskStatus;
import dev.guillermo.tasks.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskService {
    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public Page<Task> list(TaskStatus status, Pageable pageable) {
        return status == null ? repo.findAll(pageable) : repo.findByStatus(status, pageable);
    }

    @Transactional(readOnly = true)
    public Task get(Long id) {
        return repo.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task create(TaskRequest req) {
        Task t = new Task();
        apply(t, req);
        return repo.save(t);
    }

    public Task update(Long id, TaskRequest req) {
        Task t = get(id);
        apply(t, req);
        return repo.save(t);
    }

    public void delete(Long id) {
        repo.delete(get(id));
    }

    private void apply(Task t, TaskRequest req) {
        t.setTitle(req.title());
        t.setDescription(req.description());
        t.setDueDate(req.dueDate());
        if (req.status() != null) {
            t.setStatus(req.status());
        }
    }
}
