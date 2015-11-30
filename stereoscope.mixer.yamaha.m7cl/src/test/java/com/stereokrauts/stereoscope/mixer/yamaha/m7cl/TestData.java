package com.stereokrauts.stereoscope.mixer.yamaha.m7cl;

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

public class TestData {
	public MessagingSysexAssociation[] testData = {

			/* PARAMETRIC EQUALIZER */
			/* frequency */
			new MessagingSysexAssociation("freq-01", normalize("F0	43	10	3E	11	01	00	3C	00	15 00 01 000000007C F7"), new MsgInputPeqF(1, 3, 1.0f)),
			new MessagingSysexAssociation("freq-02", normalize("F0	43	10	3E	11	01	00	3C	00	15 00 01 0000000005 F7"), new MsgInputPeqF(1, 3, 0.0f)),
			new MessagingSysexAssociation("freq-03", normalize("F0	43	10	3E	11	01	00	3C	00	15 00 00 0000000005 F7"), new MsgInputPeqF(0, 3, 0.0f)),
			new MessagingSysexAssociation("freq-04", normalize("F0	43	10	3E	11	01	00	3C	00	15 00 7F 0000000005 F7"), new MsgInputPeqF(127, 3, 0.0f)),

			/* gain */
			new MessagingSysexAssociation("gain-01", normalize("F0	43	10	3E	11	01	00	3C	00	16 00 00  7F 7F 7F 7E 4C  F7"), new MsgInputPeqG(0, 3, -1.0f)), // -180
			new MessagingSysexAssociation("gain-02", normalize("F0	43	10	3E	11	01	00	3C	00	16 00 00  00 00 00 01 34  F7"), new MsgInputPeqG(0, 3, 1.0f)),  // +180
			new MessagingSysexAssociation("gain-03", normalize("F0	43	10	3E	11	01	00	3C	00	16 00 00  00 00 00 00 00  F7"), new MsgInputPeqG(0, 3, 0.0f)),  //    0

			/* quality */
			new MessagingSysexAssociation("qual-01", normalize("F0	43	10	3E	11	01	00	3C	00	14 00 00  00 00 00 00 28  F7"), new MsgInputPeqQ(0, 3, 1.0f)),  // +40
			new MessagingSysexAssociation("qual-02", normalize("F0	43	10	3E	11	01	00	3C	00	14 00 00  00 00 00 00 00  F7"), new MsgInputPeqQ(0, 3, 0.0f)),  //  0

			/* GATE */
			/* threshold */
			new MessagingSysexAssociation("dyn1-thr-01", normalize("F0	43	10	3E	11	01	00	36	00	10  00 00  7f 7f 7f 7A 30  F7"), new MsgInputDynaThreshold(0, 0, 0.0f)),  //  -720
			new MessagingSysexAssociation("dyn1-thr-02", normalize("F0	43	10	3E	11	01	00	36	00	10  00 00  00 00 00 00 00  F7"), new MsgInputDynaThreshold(0, 0, 1.0f)),  //     0
			new MessagingSysexAssociation("dyn1-thr-03", normalize("F0	43	10	3E	11	01	00	36	00	10  00 00  7f 7f 7f 7D 18  F7"), new MsgInputDynaThreshold(0, 0, 0.5f)),  //  -360

			/* range */
			new MessagingSysexAssociation("dyn1-rng-01", normalize("F0	43	10	3E	11	01	00	36	00	0A  00  00  7f 7f 7f 7f 3A  F7"), new MsgInputDynaRange(0, 0, 0.0f)),  //  -70
			new MessagingSysexAssociation("dyn1-rng-02", normalize("F0	43	10	3E	11	01	00	36	00	0A  00  00  00 00 00 00 00  F7"), new MsgInputDynaRange(0, 0, 1.0f)),  //    0

			/* attack */
			new MessagingSysexAssociation("dyn1-att-01", normalize("F0	43	10	3E	11	01	00	36	00	09  00  00  00 00 00 00 00  F7"), new MsgInputDynaAttack(0, 0, 0.0f)),  //    0
			new MessagingSysexAssociation("dyn1-att-02", normalize("F0	43	10	3E	11	01	00	36	00	09  00  00  00 00 00 00 78  F7"), new MsgInputDynaAttack(0, 0, 1.0f)),  //  120

			/* hold */
			new MessagingSysexAssociation("dyn1-hld-01", normalize("F0	43	10	3E	11	01	00	36	00	0B  00  00  00 00 00 00 00  F7"), new MsgInputDynaHold(0, 0, 0.0f)),  //    0
			new MessagingSysexAssociation("dyn1-hld-02", normalize("F0	43	10	3E	11	01	00	36	00	0B  00  00  00 00 00 01 57  F7"), new MsgInputDynaHold(0, 0, 1.0f)),  //  215

			/* decay */
			new MessagingSysexAssociation("dyn1-dcy-01", normalize("F0	43	10	3E	11	01	00	36	00	0C  00  00  00 00 00 00 00  F7"), new MsgInputDynaDecayRelease(0, 0, 0.0f)),  //  0
			new MessagingSysexAssociation("dyn1-dcy-02", normalize("F0	43	10	3E	11	01	00	36	00	0C  00  00  00 00 00 01 1F  F7"), new MsgInputDynaDecayRelease(0, 0, 1.0f)),  //  159

			/* COMPRESSOR */
			/* threshold */
			new MessagingSysexAssociation("dyn2-thr-01", normalize("F0	43	10	3E	11	01	00	39	00	10  00  00  7f 7f 7f 7B 64  F7"), new MsgInputDynaThreshold(1, 0, 0.0f)),  //  -540
			new MessagingSysexAssociation("dyn2-thr-02", normalize("F0	43	10	3E	11	01	00	39	00	10  00  00  00 00 00 00 00  F7"), new MsgInputDynaThreshold(1, 0, 1.0f)),  //     0
			new MessagingSysexAssociation("dyn2-thr-03", normalize("F0	43	10	3E	11	01	00	39	00	10  00  00  7f 7f 7f 7D 72  F7"), new MsgInputDynaThreshold(1, 0, 0.5f)),  //  -270


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

			/* DCA */
			new MessagingSysexAssociation("dca-fader-01", normalize("F0	43	10	3E	11	01	00	79	00  00 00 00 00 00 00 00 00  F7"), new MsgDcaLevelChanged(0, 0.0f)),  //  0
			new MessagingSysexAssociation("dca-fader-02", normalize("F0	43	10	3E	11	01	00	79	00  00 00 00 00 00 00 07 7F  F7"), new MsgDcaLevelChanged(0, 1.0f)),  //  1023
	};


}
