package com.dotcms.elasticsearch;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesRequestBuilder;
import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesResponse;
import org.elasticsearch.action.admin.cluster.snapshots.restore.RestoreSnapshotRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.RepositoryMetaData;
import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.snapshots.SnapshotInfo;

import com.dotcms.content.elasticsearch.util.ESClient;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.util.ConfigUtils;
import com.dotmarketing.util.Logger;

public class SnapshotAndRestore {

	final String backupPath;

	/**
	 * Path needs to point to the shared network store that
	 * all nodes can connect to
	 * 
	 * @param path
	 */
	public SnapshotAndRestore(String path) {
		backupPath = path;
	}

	public SnapshotAndRestore() {
		backupPath = ConfigUtils.getDynamicContentPath() + File.separator + "esbackup" + File.separator;
	}

	public boolean isRepositoryExist(Client client, String repositoryName) {
		boolean result = false;
		ImmutableList<RepositoryMetaData> repositories = client.admin().cluster().prepareGetRepositories().get().repositories();
		if (repositories.size() > 0) {
			for (RepositoryMetaData repo : repositories)
				result = repositoryName.equals(repo.name()) ? true : false;
		}
		return result;
	}

	public boolean createRepository(Client client, String repositoryName, boolean compress) {
		boolean result = false;
		if (!isRepositoryExist(client, repositoryName)) {
			Settings settings = ImmutableSettings.settingsBuilder().put("location", backupPath + repositoryName).put("compress", compress)
					.build();
			client.admin().cluster().preparePutRepository(repositoryName).setType("fs").setSettings(settings).get();
			Logger.info(this.getClass(), "Repository was created.");
			result = true;
		} else{
			Logger.info(this.getClass(), repositoryName + " repository already exists");
		}
		return result;

	}

	public boolean deleteRepository(Client client, String repositoryName) {

		boolean result = false;
		if (isRepositoryExist(client, repositoryName)) {
			client.admin().cluster().prepareDeleteRepository(repositoryName).execute().actionGet();
			Logger.info(this.getClass(), repositoryName + " repository has been deleted.");
			result= true;
		}

		return result;

	}

	public boolean isSnapshotExist(Client client, String repositoryName, String snapshotName) {
		boolean result = false;

		ImmutableList<SnapshotInfo> snapshotInfo = client.admin().cluster().prepareGetSnapshots(repositoryName).get().getSnapshots();
		if (snapshotInfo.size() > 0) {
			for (SnapshotInfo snapshot : snapshotInfo)
				result = snapshotName.equals(snapshot.name()) ? true : false;
		}

		return result;

	}

	public boolean createSnapshot(Client client, String repositoryName, String snapshotName, String indexName) {
		boolean result=false;

		if (isSnapshotExist(client, repositoryName, snapshotName)){
			Logger.warn(this.getClass(), snapshotName + " snapshot already exists");
			result=false;
		}
		else {
			client.admin().cluster().prepareCreateSnapshot(repositoryName, snapshotName)
					.setWaitForCompletion(true).setIndices(indexName).get();
			Logger.info(this.getClass(), "Snapshot was created:" + snapshotName);
			result=true;
		}
		return result;

	}

	public List<RepositoryMetaData> listRepositories(Client client) {
		GetRepositoriesRequestBuilder getRepo = new GetRepositoriesRequestBuilder(client.admin().cluster());
		GetRepositoriesResponse repositoryMetaDatas = getRepo.execute().actionGet();
		return repositoryMetaDatas.repositories();
	}

	public boolean deleteSnapshot(Client client, String repositoryName, String snapshotName) {
		boolean result=false;

		if (isSnapshotExist(client, repositoryName, snapshotName)) {
			client.admin().cluster().prepareDeleteSnapshot(repositoryName, snapshotName).execute().actionGet();
			Logger.info(this.getClass(), snapshotName + " snapshot has been deleted.");
		}

		return result;

	}

	public boolean restoreSnapshot(Client client, String repositoryName, String snapshotName) throws InterruptedException, ExecutionException {
		boolean result=false;

		if (isRepositoryExist(client, repositoryName) && isSnapshotExist(client, repositoryName, snapshotName)) {
			RestoreSnapshotRequest restoreSnapshotRequest = new RestoreSnapshotRequest(repositoryName, snapshotName);
			client.admin().cluster().restoreSnapshot(restoreSnapshotRequest).get();
			Logger.info(this.getClass(), "Snapshot was restored.");
		}

		return result;

	}

	public boolean snapshotAllIndex() {
		boolean result=true;
		Client client = new ESClient().getClient();

		String repo = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

		Set<String> indexes = APILocator.getESIndexAPI().listIndices();
		createRepository(client, repo, true);
		for (String index : indexes) {
			String snapshotName = index + "_snapshot";
			createSnapshot(client, repo, snapshotName, index);
		}
		return result;
	}
}