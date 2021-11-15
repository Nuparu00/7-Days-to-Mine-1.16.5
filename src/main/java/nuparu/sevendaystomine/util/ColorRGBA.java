package nuparu.sevendaystomine.util;

public class ColorRGBA {

	public double R;
	public double G;
	public double B;
	public double A;

	public ColorRGBA(double r, double g, double b, double a) {
		this.R = r;
		this.G = g;
		this.B = b;
		this.A = a;
	}

	public ColorRGBA(double r, double g, double b) {
		this.R = r;
		this.G = g;
		this.B = b;
		this.A = 1;
	}
	
	public ColorRGBA(int r, int g, int b) {
		this.R = r/255d;
		this.G = g/255d;
		this.B = b/255d;
		this.A = 1;
	}

	public ColorRGBA darken(double delta) {

		double r = MathUtils.clamp(R - delta, 0d, 1d);
		double g = MathUtils.clamp(G - delta, 0d, 1d);
		double b = MathUtils.clamp(B - delta, 0d, 1d);

		return new ColorRGBA(r, g, b, A);
	}

	public ColorRGBA lighten(double delta) {

		double r = MathUtils.clamp(R + delta, 0d, 1d);
		double g = MathUtils.clamp(G + delta, 0d, 1d);
		double b = MathUtils.clamp(B + delta, 0d, 1d);

		return new ColorRGBA(r, g, b, A);
	}

	public int toHex(){

		int r = (int) (this.R*255);
		int g = (int) (this.G*255);
		int b = (int) (this.B*255);

		return (0xff << 24) | ((r&0xff) << 16) | ((g&0xff) << 8) | (b&0xff);
	}
}
