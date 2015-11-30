package com.stereokrauts.stereoscope.mixer.yamaha.pm5d;

import static com.stereokrauts.lib.binary.ByteStringUtil.normalize;

import com.stereokrauts.stereoscope.helper.tests.MessagingSysexAssociation;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAttack;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaDecayRelease;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaHold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaThreshold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqF;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;
import com.stereokrauts.stereoscope.model.messaging.message.mixerglobal.MsgDcaLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;


public class TestData {
	public MessagingSysexAssociation[] testData = {

			/* PARAMETRIC EQUALIZER */
			/* frequency */
			new MessagingSysexAssociation("freq-01", normalize("F0 43  10  3E  0F  01 31 16 02 000000007C F7"), new MsgInputPeqF(1, 3, 1.0f)),
			new MessagingSysexAssociation("freq-02", normalize("F0 43  10  3E  0F  01 31 16 02 0000000005 F7"), new MsgInputPeqF(1, 3, 0.0f)),
			new MessagingSysexAssociation("freq-03", normalize("F0 43  10  3E  0F  01 31 16 01 0000000005 F7"), new MsgInputPeqF(0, 3, 0.0f)),
			new MessagingSysexAssociation("freq-04", normalize("F0 43  10  3E  0F  01 31 16 7F 0000000005 F7"), new MsgInputPeqF(126, 3, 0.0f)),

			/* gain */
			new MessagingSysexAssociation("gain-01", normalize("F0 43  10  3E  0F  01 31 17  01  7F 7F 7F 7E 4C  F7"), new MsgInputPeqG(0, 3, -1.0f)), // -180
			new MessagingSysexAssociation("gain-02", normalize("F0 43  10  3E  0F  01 31 17  01  00 00 00 01 34  F7"), new MsgInputPeqG(0, 3, 1.0f)),  // +180
			new MessagingSysexAssociation("gain-03", normalize("F0 43  10  3E  0F  01 31 17  01  00 00 00 00 00  F7"), new MsgInputPeqG(0, 3, 0.0f)),  //    0

			/* quality */
			new MessagingSysexAssociation("qual-01", normalize("F0 43  10  3E  0F  01	31	15  01  00 00 00 00 28  F7"), new MsgInputPeqQ(0, 3, 1.0f)),  // +40
			new MessagingSysexAssociation("qual-02", normalize("F0 43  10  3E  0F  01	31	15  01  7f 7f 7f 7f 7C  F7"), new MsgInputPeqQ(0, 3, 0.0f)),  //  -4

			/* GATE */
			/* threshold */
			new MessagingSysexAssociation("gate-thr-01", normalize("F0	43	10	3E	0F	01	2B	0D  01  7f 7f 7f 7A 30  F7"), new MsgInputDynaThreshold(0, 0, 0.0f)),  //  -720
			new MessagingSysexAssociation("gate-thr-02", normalize("F0	43	10	3E	0F	01	2B	0D  01  00 00 00 00 00  F7"), new MsgInputDynaThreshold(0, 0, 1.0f)),  //     0
			new MessagingSysexAssociation("gate-thr-03", normalize("F0	43	10	3E	0F	01	2B	0D  01  7f 7f 7f 7D 18  F7"), new MsgInputDynaThreshold(0, 0, 0.5f)),  //  -360

			/* range */
			new MessagingSysexAssociation("gate-rng-01", normalize("F0	43	10	3E	0F	01	2B	0A  01  7f 7f 7f 7f 3A  F7"), new MsgInputDynaRange(0, 0, 0.0f)),  //  -70
			new MessagingSysexAssociation("gate-rng-02", normalize("F0	43	10	3E	0F	01	2B	0A  01  00 00 00 00 00  F7"), new MsgInputDynaRange(0, 0, 1.0f)),  //    0

			/* attack */
			new MessagingSysexAssociation("gate-att-01", normalize("F0	43	10	3E	0F	01	2B	09  01  00 00 00 00 00  F7"), new MsgInputDynaAttack(0, 0, 0.0f)),  //    0
			new MessagingSysexAssociation("gate-att-02", normalize("F0	43	10	3E	0F	01	2B	09  01  00 00 00 00 78  F7"), new MsgInputDynaAttack(0, 0, 1.0f)),  //  120

			/* hold */
			new MessagingSysexAssociation("gate-hld-01", normalize("F0	43	10	3E	0F	01	2B	0B  01  00 00 00 00 00  F7"), new MsgInputDynaHold(0, 0, 0.0f)),  //    0
			new MessagingSysexAssociation("gate-hld-02", normalize("F0	43	10	3E	0F	01	2B	0B  01  00 00 00 01 57  F7"), new MsgInputDynaHold(0, 0, 1.0f)),  //  215

			/* decay */
			new MessagingSysexAssociation("gate-dcy-01", normalize("F0	43	10	3E	0F	01	2B	0C  01  00 00 00 00 00  F7"), new MsgInputDynaDecayRelease(0, 0, 0.0f)),  //  0
			new MessagingSysexAssociation("gate-dcy-02", normalize("F0	43	10	3E	0F	01	2B	0C  01  00 00 00 01 1F  F7"), new MsgInputDynaDecayRelease(0, 0, 1.0f)),  //  159

			/* COMPRESSOR */
			/* threshold */
			new MessagingSysexAssociation("comp-thr-01", normalize("F0	43	10	3E	0F	01	2E	0A  01  7f 7f 7f 7B 64  F7"), new MsgInputDynaThreshold(1, 0, 0.0f)),  //  -540
			new MessagingSysexAssociation("comp-thr-02", normalize("F0	43	10	3E	0F	01	2E	0A  01  00 00 00 00 00  F7"), new MsgInputDynaThreshold(1, 0, 1.0f)),  //     0
			new MessagingSysexAssociation("comp-thr-03", normalize("F0	43	10	3E	0F	01	2E	0A  01  7f 7f 7f 7D 72  F7"), new MsgInputDynaThreshold(1, 0, 0.5f)),  //  -270


			/* range */
			//new MessagingSysexAssociation("comp-rng-01", normalize("F0	43	10	3E	0F	01	2B	0A  01  7f 7f 7f 7f 3A  F7"), new MsgInputDynaRange(0, 0, 0.0f)),  //  -70
			//new MessagingSysexAssociation("comp-rng-02", normalize("F0	43	10	3E	0F	01	2B	0A  01  00 00 00 00 00  F7"), new MsgInputDynaRange(0, 0, 1.0f)),  //    0

			/* attack */
			//new MessagingSysexAssociation("comp-att-01", normalize("F0	43	10	3E	0F	01	2B	09  01  00 00 00 00 00  F7"), new MsgInputDynaAttack(0, 0, 0.0f)),  //    0
			//new MessagingSysexAssociation("comp-att-02", normalize("F0	43	10	3E	0F	01	2B	09  01  00 00 00 00 78  F7"), new MsgInputDynaAttack(0, 0, 1.0f)),  //  120

			/* hold */
			//new MessagingSysexAssociation("comp-hld-01", normalize("F0	43	10	3E	0F	01	2B	0B  01  00 00 00 00 00  F7"), new MsgInputDynaHold(0, 0, 0.0f)),  //    0
			//new MessagingSysexAssociation("comp-hld-02", normalize("F0	43	10	3E	0F	01	2B	0B  01  00 00 00 01 57  F7"), new MsgInputDynaHold(0, 0, 1.0f)),  //  215

			/* decay */
			//new MessagingSysexAssociation("comp-dcy-01", normalize("F0	43	10	3E	0F	01	2B	0C  01  00 00 00 00 00  F7"), new MsgInputDynaDecayRelease(0, 0, 0.0f)),  //  0
			//new MessagingSysexAssociation("comp-dcy-02", normalize("F0	43	10	3E	0F	01	2B	0C  01  00 00 00 01 1F  F7"), new MsgInputDynaDecayRelease(0, 0, 1.0f)),  //  159

			/* AUX */
			//new MessagingSysexAssociation("aux-snd-lvl-01", normalize("F0	43	10	3E	7F	01	23	02	00	00	00	00	00	F7"), new MsgAuxSendChanged(1, 1, 0.0f)),  // 0
			//new MessagingSysexAssociation("aux-snd-lvl-02", normalize("F0	43	10	3E	7F	01	23	05	00	00	00	"), new MsgAuxSendChanged(1, 2, 0.5f)),  // 511
			//new MessagingSysexAssociation("aux-snd-lvl-01", normalize(""), new MsgAuxSendChanged(1, 3, 1.0f)),  // 1023
			new MessagingSysexAssociation("mix-master-01", normalize("F0	43	10	3E	0F	01	3F	01	01	00	00	00	07	7F	F7"), new MsgAuxMasterLevelChanged(0, 1.0f)),  //   1.0f
			new MessagingSysexAssociation("mix-master-02", normalize("F0	43	10	3E	0F	01	3F	01	01	00	00	00	00	00	F7"), new MsgAuxMasterLevelChanged(0, 0.0f)),  //   1.0f


			/* DCA */
			new MessagingSysexAssociation("dca-fader-01", normalize("F0	43	10	3E	0F	01	70	01	01	00	00	00	07	7F	F7"), new MsgDcaLevelChanged(0, 1.0f)),  //   1.0f
			new MessagingSysexAssociation("dca-fader-02", normalize("F0	43	10	3E	0F	01	70	01	01	00	00	00	00	00	F7"), new MsgDcaLevelChanged(0, 0.0f)),  //   1.0f

	};


}
