package util;

import protonova.protobuf.VectorProto.Vector;

public class DebugPrinter {

	public static void print(Vector vector) {
		System.out.println(vector.getX()+","+vector.getY());
	}
}
