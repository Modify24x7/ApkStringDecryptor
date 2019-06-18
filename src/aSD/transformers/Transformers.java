package aSD.transformers;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.ReferenceType;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.MutableMethodImplementation;
import org.jf.dexlib2.builder.instruction.BuilderInstruction10x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21c;
import org.jf.dexlib2.builder.instruction.BuilderInstruction31c;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.immutable.ImmutableClassDef;
import org.jf.dexlib2.immutable.ImmutableMethod;
import org.jf.dexlib2.immutable.ImmutableMethodImplementation;
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction11x;
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference;
import org.jf.dexlib2.immutable.reference.ImmutableStringReference;

import com.google.common.collect.ImmutableList;

/**
 * @author Gorav Gupta
 *
 */
public class Transformers {

	public static void decryptStrings(String inDex, String outDex, int API, String callMethodRef, boolean removeCall,
			ImmutableMethodReference immutableMethodReference) throws Exception {

		DexFile dexFile = DexFileFactory.loadDexFile(inDex, Opcodes.forApi(API));

		List<ClassDef> classes = new ArrayList<>();

		for (ClassDef classDef : dexFile.getClasses()) {

			List<Method> methods = new ArrayList<>();
			boolean modifiedMethod = false;

			for (Method method : classDef.getMethods()) {
				MethodImplementation implementation = method.getImplementation();

				if (!removeCall && method.equals(immutableMethodReference)) {
					modifiedMethod = true;
					methods.add(new ImmutableMethod(method.getDefiningClass(), method.getName(), method.getParameters(),
							method.getReturnType(), method.getAccessFlags(), method.getAnnotations(),
							new ImmutableMethodImplementation(1,
									ImmutableList.of(new ImmutableInstruction11x(Opcode.RETURN_OBJECT, 0)), null,
									null)));

				} else if (implementation != null && methodNeedsModification(implementation)) {
					modifiedMethod = true;
					StringDecryptor.method.add(method);
					methods.add(new ImmutableMethod(method.getDefiningClass(), method.getName(), method.getParameters(),
							method.getReturnType(), method.getAccessFlags(), method.getAnnotations(),
							modifyMethod(implementation, callMethodRef, removeCall)));
					StringDecryptor.method.remove(0);
				} else {
					methods.add(method);
				}
			}

			if (!modifiedMethod) {
				classes.add(classDef);
			} else {
				classes.add(new ImmutableClassDef(classDef.getType(), classDef.getAccessFlags(),
						classDef.getSuperclass(), classDef.getInterfaces(), classDef.getSourceFile(),
						classDef.getAnnotations(), classDef.getFields(), methods));
			}
		}

		writeDexFile(classes, outDex, API);
		System.out.println("https://github.com/Modify24x7");
	}

	static boolean methodNeedsModification(@Nonnull MethodImplementation implementation) {
		for (Instruction instruction : implementation.getInstructions()) {
			if (instruction instanceof ReferenceInstruction) {
				if (((ReferenceInstruction) instruction).getReferenceType() == ReferenceType.STRING) {
					return true;
				}
			}
		}
		return false;
	}

	static MethodImplementation modifyMethod(@Nonnull MethodImplementation implementation, String callMethodRef,
			boolean removeCall) {

		MutableMethodImplementation mutableImplementation = new MutableMethodImplementation(implementation);
		List<BuilderInstruction> instructions = mutableImplementation.getInstructions();
		ArrayList<Integer> callList = new ArrayList<>();

		// Analyze
		for (int i = 0; i < instructions.size(); i++) {
			Instruction instruction = instructions.get(i);
			if (instruction instanceof ReferenceInstruction) {
				String call = ((ReferenceInstruction) instruction).getReference().toString();
				if (call.equals(callMethodRef)) {
					callList.add(i);
				}
			}
		}

		// Decode and replace string
		for (int i = 0; i < instructions.size(); i++) {
			Instruction instruction = instructions.get(i);
			if (instruction instanceof ReferenceInstruction) {
				if (((ReferenceInstruction) instruction).getReferenceType() == ReferenceType.STRING) {
					if (repTrue(callList, i)) {
						String str = ((ReferenceInstruction) instruction).getReference().toString();
						Opcode opcode = instruction.getOpcode();

						int register = ((OneRegisterInstruction) instruction).getRegisterA();

						if (opcode == Opcode.CONST_STRING_JUMBO)
							mutableImplementation.replaceInstruction(i, new BuilderInstruction31c(opcode, register,
									new ImmutableStringReference(StringDecryptor.decryptor(str))));
						else
							mutableImplementation.replaceInstruction(i, new BuilderInstruction21c(opcode, register,
									new ImmutableStringReference(StringDecryptor.decryptor(str))));
					}
				}
			}
		}

		if (removeCall) {
			// Replace Method Call to NOP
			for (int i = 0; i < instructions.size(); i++) {
				Instruction instruction = instructions.get(i);
				if (instruction instanceof ReferenceInstruction) {
					String call = ((ReferenceInstruction) instruction).getReference().toString();
					if (call.equals(callMethodRef)) {
						mutableImplementation.replaceInstruction(i, new BuilderInstruction10x(Opcode.NOP));
					}
				}
			}

			// Replace MOVE_RESULT_OBJECT to NOP
			for (int i = 0; i < instructions.size(); i++) {
				if (repTrue2(callList, i)) {
					mutableImplementation.replaceInstruction(i, new BuilderInstruction10x(Opcode.NOP));
				}
			}
		}

		return mutableImplementation;
	}

	// Replace String
	static boolean repTrue(ArrayList<Integer> callList, int ins) {
		for (int i = 0; i < callList.size(); i++) {
			if (ins == callList.get(i) - 1) {
				return true;
			}
		}
		return false;
	}

	// Replace MOVE_RESULT_OBJECT
	static boolean repTrue2(ArrayList<Integer> callList, int ins) {
		for (int i = 0; i < callList.size(); i++) {
			if (ins == callList.get(i) + 1) {
				return true;
			}
		}
		return false;
	}

	// Write Output Dex
	static void writeDexFile(List<ClassDef> classes, String outDex, int API) throws Exception {

		Collections.sort(classes);

		DexFileFactory.writeDexFile(outDex, new DexFile() {

			@Nonnull
			@Override
			public Set<? extends ClassDef> getClasses() {
				// TODO Auto-generated method stub
				return new AbstractSet<ClassDef>() {

					@Override
					public Iterator<ClassDef> iterator() {
						// TODO Auto-generated method stub
						return classes.iterator();
					}

					@Override
					public int size() {
						// TODO Auto-generated method stub
						return classes.size();
					}

				};
			}

			@Override
			public Opcodes getOpcodes() {
				// TODO Auto-generated method stub
				return Opcodes.forApi(API);
			}
		});
	}

}
