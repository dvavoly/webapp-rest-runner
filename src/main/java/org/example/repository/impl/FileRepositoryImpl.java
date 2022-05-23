package org.example.repository.impl;

import org.example.entity.File;
import org.example.repository.FileRepository;
import org.example.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class FileRepositoryImpl implements FileRepository {

    private static final FileRepository INSTANCE = new FileRepositoryImpl();

    private FileRepositoryImpl() {
    }

    @Override
    public List<File> findAll() {
        try (var session = HibernateUtil.openSession()) {
            return session.createQuery("select f from File f", File.class).list();
        }
    }

    @Override
    public Optional<File> findById(Integer id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Id cannot be null or negative number");
        }
        try (var session = HibernateUtil.openSession()) {
            var query = session.createQuery("select f from File f where f.id=:id", File.class);
            query.setParameter("id", id);
            return query.uniqueResultOptional();
        }
    }

    @Override
    public File save(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        try (var session = HibernateUtil.openSession()) {
            var tx = session.beginTransaction();
            session.persist(file);
            tx.commit();
        }
        return file;
    }

    @Override
    public File update(File file) {
        if (file == null || file.getId() == null) {
            throw new IllegalArgumentException("File or file id cannot be null");
        }
        try (var session = HibernateUtil.openSession()) {
            var tx = session.beginTransaction();
            var foundFile = session.find(File.class, file.getId());
            if (foundFile != null) {
                foundFile.setFileName(file.getFileName());
                foundFile.setFileType(file.getFileType());
                foundFile.setFileDownloadUri(file.getFileDownloadUri());
                session.persist(foundFile);
                tx.commit();
                return foundFile;
            }
        }
        return file;
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        try (var session = HibernateUtil.openSession()) {
            var tx = session.beginTransaction();
            var foundFile = findById(id);
            foundFile.ifPresent(session::remove);
            tx.commit();
        }
    }

    public static FileRepository getInstance() {
        return INSTANCE;
    }
}
