package org.hive2hive.core.process.common.put;

import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.hive2hive.core.H2HConstants;
import org.hive2hive.core.log.H2HLogger;
import org.hive2hive.core.log.H2HLoggerFactory;
import org.hive2hive.core.model.MetaDocument;
import org.hive2hive.core.model.MetaFile;
import org.hive2hive.core.model.MetaFolder;
import org.hive2hive.core.process.ProcessStep;
import org.hive2hive.core.security.H2HEncryptionUtil;
import org.hive2hive.core.security.HybridEncryptedContent;

/**
 * Puts a {@link MetaFile} or a {@link MetaFolder} object into the DHT after encrypting it with the given key.
 * 
 * @author Nico
 * 
 */
public class PutMetaDocumentStep extends BasePutProcessStep {

	private static final H2HLogger logger = H2HLoggerFactory.getLogger(PutMetaDocumentStep.class);
	protected MetaDocument metaDocument;

	public PutMetaDocumentStep(MetaDocument metaDocument, ProcessStep nextStep) {
		super(nextStep);
		this.metaDocument = metaDocument;
	}

	@Override
	public void start() {
		try {
			logger.debug("Encrypting meta document in a hybrid manner");
			HybridEncryptedContent encrypted = H2HEncryptionUtil.encryptHybrid(metaDocument,
					metaDocument.getId());
			put(key2String(metaDocument.getId()), H2HConstants.META_DOCUMENT, encrypted);
		} catch (DataLengthException | InvalidKeyException | IllegalStateException
				| InvalidCipherTextException | IllegalBlockSizeException | BadPaddingException e) {
			getProcess().stop("Meta document could not be encrypted");
		}
	}
}
