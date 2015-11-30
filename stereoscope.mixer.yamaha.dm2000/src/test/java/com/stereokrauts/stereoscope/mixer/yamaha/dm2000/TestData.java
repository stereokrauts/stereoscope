package com.stereokrauts.stereoscope.mixer.yamaha.dm2000;

import static com.stereokrauts.lib.binary.ByteStringUtil.normalize;

import com.stereokrauts.stereoscope.helper.tests.MessagingSysexAssociation;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAttack;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaDecayRelease;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaGain;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaHold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKnee;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRatio;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaThreshold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqF;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterLevelChanged;

public class TestData {
	public MessagingSysexAssociation[] testData = {

			/* PARAMETRIC EQUALIZER */
			/* frequency */
			new MessagingSysexAssociation("freq-01", normalize("F0	43	10	3E	7F	01	20	0C 01 0000007C F7"), new MsgInputPeqF(1, 3, 1.0f)),
			new MessagingSysexAssociation("freq-02", normalize("F0	43	10	3E	7F	01	20	0C 01 00000005 F7"), new MsgInputPeqF(1, 3, 0.0f)),
			new MessagingSysexAssociation("freq-03", normalize("F0	43	10	3E	7F	01	20	0C 00 00000005 F7"), new MsgInputPeqF(0, 3, 0.0f)),
			new MessagingSysexAssociation("freq-04", normalize("F0	43	10	3E	7F	01	20	0C 7F 00000005 F7"), new MsgInputPeqF(127, 3, 0.0f)),

			/* gain */
			new MessagingSysexAssociation("gain-01", normalize("F0	43	10	3E	7F	01	20	0D 00  7F 7F 7E 4C  F7"), new MsgInputPeqG(0, 3, -1.0f)), // -180
			new MessagingSysexAssociation("gain-02", normalize("F0	43	10	3E	7F	01	20	0D 00  00 00 01 34  F7"), new MsgInputPeqG(0, 3, 1.0f)),  // +180
			new MessagingSysexAssociation("gain-03", normalize("F0	43	10	3E	7F	01	20	0D 00  00 00 00 00  F7"), new MsgInputPeqG(0, 3, 0.0f)),  //    0

			/* quality */
			// FIXME: The maximum value on the DM2000 is 43, not 40. The real last sysex value must be 2B instead of 28.
			new MessagingSysexAssociation("qual-01", normalize("F0	43	10	3E	7F	01	20	0B 00 00 00 00 28  F7"), new MsgInputPeqQ(0, 3, 1.0f)),  // +43
			new MessagingSysexAssociation("qual-02", normalize("F0	43	10	3E	7F	01	20	0B 00 00 00 00 00  F7"), new MsgInputPeqQ(0, 3, 0.0f)),  //  0

			/* GATE */
			/* threshold */
			new MessagingSysexAssociation("dyn1-thr-01", normalize("F0	43	10	3E	7F	01	1E	0A 00  7f 7f 7B 64  F7"), new MsgInputDynaThreshold(0, 0, 0.0f)),  //  -540
			new MessagingSysexAssociation("dyn1-thr-02", normalize("F0	43	10	3E	7F	01	1E	0A 00  00 00 00 00  F7"), new MsgInputDynaThreshold(0, 0, 1.0f)),  //     0
			new MessagingSysexAssociation("dyn1-thr-03", normalize("F0	43	10	3E	7F	01	1E	0A 00  7f 7f 7D 72  F7"), new MsgInputDynaThreshold(0, 0, 0.5f)),  //  -270

			/* range */
			new MessagingSysexAssociation("dyn1-rng-01", normalize("F0	43	10	3E	7F	01	1E	07  00  7f 7f 7f 3A  F7"), new MsgInputDynaRange(0, 0, 0.0f)),  //  -70
			new MessagingSysexAssociation("dyn1-rng-02", normalize("F0	43	10	3E	7F	01	1E	07  00  00 00 00 00  F7"), new MsgInputDynaRange(0, 0, 1.0f)),  //    0

			/* attack */
			new MessagingSysexAssociation("dyn1-att-01", normalize("F0	43	10	3E	7F	01	1E	06  00  00 00 00 00  F7"), new MsgInputDynaAttack(0, 0, 0.0f)),  //    0
			new MessagingSysexAssociation("dyn1-att-02", normalize("F0	43	10	3E	7F	01	1E	06  00  00 00 00 78  F7"), new MsgInputDynaAttack(0, 0, 1.0f)),  //  120

			/* hold */
			new MessagingSysexAssociation("dyn1-hld-01", normalize("F0	43	10	3E	7F	01	1E	08  00  00 00 00 00  F7"), new MsgInputDynaHold(0, 0, 0.0f)),  //    0
			new MessagingSysexAssociation("dyn1-hld-02", normalize("F0	43	10	3E	7F	01	1E	08  00  00 00 01 57  F7"), new MsgInputDynaHold(0, 0, 1.0f)),  //  215

			/* decay */
			new MessagingSysexAssociation("dyn1-dcy-01", normalize("F0	43	10	3E	7F	01	1E	09  00  00 00 00 00  F7"), new MsgInputDynaDecayRelease(0, 0, 0.0f)),  //  0
			new MessagingSysexAssociation("dyn1-dcy-02", normalize("F0	43	10	3E	7F	01	1E	09  00  00 00 01 1F  F7"), new MsgInputDynaDecayRelease(0, 0, 1.0f)),  //  159

			/* COMPRESSOR */
			/* threshold */
			new MessagingSysexAssociation("dyn2-thr-01", normalize("F0	43	10	3E	7F	01	1F	09  00  7f 7f 7B 64  F7"), new MsgInputDynaThreshold(1, 0, 0.0f)),  //  -540
			new MessagingSysexAssociation("dyn2-thr-02", normalize("F0	43	10	3E	7F	01	1F	09  00  00 00 00 00  F7"), new MsgInputDynaThreshold(1, 0, 1.0f)),  //     0
			new MessagingSysexAssociation("dyn2-thr-03", normalize("F0	43	10	3E	7F	01	1F	09  00  7f 7f 7D 72  F7"), new MsgInputDynaThreshold(1, 0, 0.5f)),  //  -270

			/* ratio */
			new MessagingSysexAssociation("dyn2-rto-01", normalize("F0	43	10	3E	7F	01	1F	06  00  00 00 00 00  F7"), new MsgInputDynaRatio(1, 0, 0.0f)),  //  0
			new MessagingSysexAssociation("dyn2-rto-02", normalize("F0	43	10	3E	7F	01	1F	06  00  00 00 00 0F  F7"), new MsgInputDynaRatio(1, 0, 1.0f)),  //  15

			/* gain */
			new MessagingSysexAssociation("dyn2-gn-01", normalize("F0	43	10	3E	7F	01	1F	07  00  00 00 00 00  F7"), new MsgInputDynaGain(1, 0, 0.0f)),  //    0
			new MessagingSysexAssociation("dyn2-gn-02", normalize("F0	43	10	3E	7F	01	1F	07  00  00 00 01 34  F7"), new MsgInputDynaGain(1, 0, 1.0f)),  //  180

			/* attack */
			new MessagingSysexAssociation("dyn2-att-01", normalize("F0	43	10	3E	7F	01	1F	04  00  00 00 00 00  F7"), new MsgInputDynaAttack(1, 0, 0.0f)),  //    0
			new MessagingSysexAssociation("dyn2-att-02", normalize("F0	43	10	3E	7F	01	1F	04  00  00 00 00 78  F7"), new MsgInputDynaAttack(1, 0, 1.0f)),  //  120

			/* release */
			new MessagingSysexAssociation("dyn2-rls-01", normalize("F0	43	10	3E	7F	01	1F	05  00  00 00 00 00  F7"), new MsgInputDynaDecayRelease(1, 0, 0.0f)),  //  0
			new MessagingSysexAssociation("dyn2-rls-02", normalize("F0	43	10	3E	7F	01	1F	05  00  00 00 01 1F  F7"), new MsgInputDynaDecayRelease(1, 0, 1.0f)),  //  159

			/* knee */
			new MessagingSysexAssociation("dyn2-kne-01", normalize("F0	43	10	3E	7F	01	1F	08  00  00 00 00 00  F7"), new MsgInputDynaKnee(1, 0, 0.0f)),  //   0
			new MessagingSysexAssociation("dyn2-kne-02", normalize("F0	43	10	3E	7F	01	1F	08  00  00 00 00 05  F7"), new MsgInputDynaKnee(1, 0, 1.0f)),  //   5



			/* AUX */
			//new MessagingSysexAssociation("aux-snd-lvl-01", normalize("F0	43	10	3E	7F	01	23	02	00	00	00	00	00	F7"), new MsgAuxSendChanged(1, 1, 0.0f)),  // 0
			//new MessagingSysexAssociation("aux-snd-lvl-02", normalize("F0	43	10	3E	7F	01	23	05	00	00	00	"), new MsgAuxSendChanged(1, 2, 0.5f)),  // 511
			//new MessagingSysexAssociation("aux-snd-lvl-01", normalize(""), new MsgAuxSendChanged(1, 3, 1.0f)),  // 1023
			new MessagingSysexAssociation("aux-master-01", normalize("F0	43	10	3E	7F	01	39	00	00	00	00	07	7F	F7"), new MsgAuxMasterLevelChanged(0, 1.0f)),  //   1.0f
			new MessagingSysexAssociation("aux-master-02", normalize("F0	43	10	3E	7F	01	39	00	00	00	00	00	00	F7"), new MsgAuxMasterLevelChanged(0, 0.0f)),  //   1.0f


			/* BUS */
			new MessagingSysexAssociation("bus-master-01", normalize("F0	43	10	3E	7F	01	2B	00	00	00	00	07	7F	F7"), new MsgBusMasterLevelChanged(0, 1.0f)),  //   1.0f
			new MessagingSysexAssociation("bus-master-02", normalize("F0	43	10	3E	7F	01	2B	00	00	00	00	00	00	F7"), new MsgBusMasterLevelChanged(0, 0.0f)),  //   1.0f

			/* CHANNEL LEVEL */
	};


}
