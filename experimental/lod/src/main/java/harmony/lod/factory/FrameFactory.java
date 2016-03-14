package harmony.lod.factory;

import harmony.lod.model.api.slice.Frame;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.impl.slice.FrameException;

public interface FrameFactory {

	public Frame get(StatementTemplate... st) throws FrameException;

	public Frame getTP(String type, String... predicates) throws FrameException;

	public Frame getP(String... predicates) throws FrameException;
}
