package cz.artique.client.manager;

import cz.artique.client.artiqueLabels.ArtiqueLabelsManager;
import cz.artique.client.artiqueSources.ArtiqueSourcesManager;
import cz.artique.client.config.ArtiqueConfigManager;

public enum Managers {
	CONFIG_MANAGER {
		@Override
		public Manager get() {
			return ArtiqueConfigManager.MANAGER;
		}
	},
	ITEMS_MANAGER {
		@Override
		public Manager get() {
			return ArtiqueLabelsManager.MANAGER;
		}
	},
	LABELS_MANAGER {
		@Override
		public Manager get() {
			return ArtiqueLabelsManager.MANAGER;
		}
	},
	SOURCES_MANAGER {
		@Override
		public Manager get() {
			return ArtiqueSourcesManager.MANAGER;
		}
	};
	public abstract Manager get();
}
