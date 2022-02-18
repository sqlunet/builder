package org.sqlbuilder.sl.foreign;

import org.sqlbuilder.pb.objects.Theta;

public class VnRole
{
	public final String vnClass;

	public final Theta theta;

	public static VnRole make(final String vnClass, final Theta theta)
	{
		return new VnRole(vnClass, theta);
	}

	private VnRole(final String vnClass, final Theta theta)
	{
		this.vnClass = vnClass;
		this.theta = theta;
	}
}
